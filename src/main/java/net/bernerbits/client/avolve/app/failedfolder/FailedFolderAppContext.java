package net.bernerbits.client.avolve.app.failedfolder;

import java.io.File;

import net.bernerbits.client.avolve.app.AppContext;
import net.bernerbits.client.avolve.model.Bucket;
import net.bernerbits.client.avolve.model.LocalFile;
import net.bernerbits.client.avolve.model.failedfolder.FailedFolderScanner;
import net.bernerbits.client.avolve.model.sheet.SpreadsheetScanner;

import org.apache.poi.ss.usermodel.Sheet;

public class FailedFolderAppContext extends AppContext {

	private final Sheet spreadsheet;
	private final SpreadsheetScanner<File> spreadsheetScanner;
	private final FailedFolderScanner failedFolderScanner;

	public FailedFolderAppContext(Sheet spreadsheet,
			SpreadsheetScanner<File> spreadsheetScanner, Bucket bucket,
			FailedFolderScanner failedFolderScanner) {
		super(bucket);
		this.spreadsheet = spreadsheet;
		this.spreadsheetScanner = spreadsheetScanner;
		this.failedFolderScanner = failedFolderScanner;
	}

	public void replaceFailedFiles() {
		for (File failedFolder : spreadsheetScanner.scan(spreadsheet)) {
			for (LocalFile failedFile : failedFolderScanner
					.scanForFiles(failedFolder)) {
				bucket.upload(failedFile);
				bucket.verifyUpstream(failedFile);
			}
		}
		System.out
				.println("All files uploaded! Please check errors.log for problems.");
	}

}
