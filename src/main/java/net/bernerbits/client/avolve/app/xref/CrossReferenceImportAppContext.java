package net.bernerbits.client.avolve.app.xref;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadFactory;

import net.bernerbits.client.avolve.app.AppContext;
import net.bernerbits.client.avolve.model.Bucket;
import net.bernerbits.client.avolve.model.LocalFile;
import net.bernerbits.client.avolve.model.UploadReport;
import net.bernerbits.client.avolve.model.sheet.SpreadsheetScanner;
import net.bernerbits.client.avolve.model.xref.ExistingFileFilter;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Sheet;

public class CrossReferenceImportAppContext extends AppContext {

	private static final int PARALLEL_THREADS = 8;

	private Logger errlog = Logger.getLogger("errlog");

	private final SpreadsheetScanner<String> existingFileScanner;
	private final SpreadsheetScanner<LocalFile> sourceFileScanner;

	private final Sheet existingSheet;
	private final Sheet sourceSheet;

	private final UploadReport uploadReport;

	public CrossReferenceImportAppContext(Bucket bucket,
			SpreadsheetScanner<String> existingFileScanner,
			SpreadsheetScanner<LocalFile> sourceFileScanner,
			Sheet existingSheet, Sheet sourceSheet, UploadReport uploadReport) {
		super(bucket);
		this.existingFileScanner = existingFileScanner;
		this.sourceFileScanner = sourceFileScanner;
		this.existingSheet = existingSheet;
		this.sourceSheet = sourceSheet;
		this.uploadReport = uploadReport;
	}

	public CrossReferenceImportAppContext(Bucket bucket,
			SpreadsheetScanner<String> existingFileScanner,
			SpreadsheetScanner<LocalFile> sourceFileScanner,
			Sheet existingSheet, Sheet sourceSheet) {
		this(bucket, existingFileScanner, sourceFileScanner, existingSheet,
				sourceSheet, new UploadReport("upload.txt"));
	}

	public void importNewFiles() {
		try {
			uploadReport.begin();
		} catch (IOException e) {
			errlog.warn("Could not create report file", e);
		}

		List<String> existingFiles = existingFileScanner.scan(existingSheet);
		List<LocalFile> sourceFiles = sourceFileScanner.scan(sourceSheet);

		sourceFiles = new ExistingFileFilter().filterExistingFiles(
				existingFiles, sourceFiles);

		// Parallel executor to upload files in parallel.
		ExecutorService es = Executors.newFixedThreadPool(PARALLEL_THREADS,
				new ThreadFactory() {
					@Override
					public Thread newThread(Runnable r) {
						Thread t = new Thread(r);
						t.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {

							@Override
							public void uncaughtException(Thread t, Throwable e) {
								errlog.error("Unexpected thread death", e);
							}
						});
						return t;
					}
				});
		final Semaphore sem = new Semaphore(PARALLEL_THREADS);

		for (final LocalFile localFile : sourceFiles) {
			sem.acquireUninterruptibly();
			es.execute(new Runnable() {
				@Override
				public void run() {
					try {
						bucket.upload(localFile);
						bucket.verifyUpstream(localFile);
						try {
							uploadReport.fileUploaded(localFile);
						} catch (IOException ex) {
							errlog.warn("Unable to write to report", ex);
						}
					} finally {
						sem.release();
					}
				}
			});

		}

		es.shutdown();
		for (int i = 0; i < PARALLEL_THREADS; i++) {
			sem.acquireUninterruptibly();
		}

		try {
			uploadReport.finish();
		} catch (IOException e) {
			errlog.warn("Unable to finalize report", e);
		}

		System.out
				.println("File upload complete. Please check errors.log for problems.");
	}
}
