package net.bernerbits.client.avolve.app.xref;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import net.bernerbits.client.avolve.model.Bucket;
import net.bernerbits.client.avolve.model.LocalFile;
import net.bernerbits.client.avolve.model.UploadReport;
import net.bernerbits.client.avolve.model.sheet.IRowMapper;
import net.bernerbits.client.avolve.model.sheet.SpreadsheetScanner;

import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class TestCrossReferenceImportAppContext {

	@Mock
	private Bucket bucket;

	@Mock
	private SpreadsheetScanner<String> existingFileScanner;

	@Mock
	private SpreadsheetScanner<LocalFile> sourceFileScanner;

	@Mock
	private Sheet existingSheet;
	@Mock
	private Sheet sourceSheet;

	@Mock
	private UploadReport uploadReport;

	@Test
	public void testImportNewFiles() throws Exception {
		CrossReferenceImportAppContext ctx = new CrossReferenceImportAppContext(
				bucket, existingFileScanner, sourceFileScanner, existingSheet,
				sourceSheet, uploadReport);
		
		List<String> existingFiles = Arrays.asList("ABC", "DEF", "GHI");

		LocalFile localFile1 = new LocalFile(new File("ABC"), "key/abc");
		LocalFile localFile2 = new LocalFile(new File("CDE"), "key/cde");
		LocalFile localFile3 = new LocalFile(new File("BCD"), "key/bcd");
		LocalFile localFile4 = new LocalFile(new File("DEF"), "key/def");

		when(existingFileScanner.scan(existingSheet)).thenReturn(existingFiles);
		
		when(sourceFileScanner.scan(sourceSheet)).thenReturn(Arrays.asList(localFile1, localFile2, localFile3, localFile4));
		
		ctx.importNewFiles();
		
		verify(bucket).upload(localFile2);
		verify(bucket).upload(localFile3);

		verify(bucket).verifyUpstream(localFile2);
		verify(bucket).verifyUpstream(localFile3);

		verifyNoMoreInteractions(bucket);
		
		verify(uploadReport).begin();
		verify(uploadReport).fileUploaded(localFile2);
		verify(uploadReport).fileUploaded(localFile3);
		verify(uploadReport).finish();
		
		verifyNoMoreInteractions(uploadReport);
	}
	
	@Test
	public void testImportNewFilesUploadRuntimeException() throws Exception {
		CrossReferenceImportAppContext ctx = new CrossReferenceImportAppContext(
				bucket, existingFileScanner, sourceFileScanner, existingSheet,
				sourceSheet, uploadReport);
		
		List<String> existingFiles = Arrays.asList("ABC", "DEF", "GHI");

		LocalFile localFile1 = new LocalFile(new File("ABC"), "key/abc");
		LocalFile localFile2 = new LocalFile(new File("CDE"), "key/cde");
		LocalFile localFile3 = new LocalFile(new File("BCD"), "key/bcd");
		LocalFile localFile4 = new LocalFile(new File("DEF"), "key/def");

		when(existingFileScanner.scan(existingSheet)).thenReturn(existingFiles);
		
		when(sourceFileScanner.scan(sourceSheet)).thenReturn(Arrays.asList(localFile1, localFile2, localFile3, localFile4));
		
		doThrow(RuntimeException.class).when(bucket).upload(localFile2);
		
		ctx.importNewFiles();
		
		verify(bucket).upload(localFile2);
		verify(bucket).upload(localFile3);

		verify(bucket).verifyUpstream(localFile3);

		verifyNoMoreInteractions(bucket);
		
		verify(uploadReport).begin();
		verify(uploadReport).fileUploaded(localFile3);
		verify(uploadReport).finish();
		
		verifyNoMoreInteractions(uploadReport);
	}
	

	@Test
	public void testImportNewFilesUploadIOException() throws Exception {
		CrossReferenceImportAppContext ctx = new CrossReferenceImportAppContext(
				bucket, existingFileScanner, sourceFileScanner, existingSheet,
				sourceSheet, uploadReport);
		
		List<String> existingFiles = Arrays.asList("ABC", "DEF", "GHI");

		LocalFile localFile1 = new LocalFile(new File("ABC"), "key/abc");
		LocalFile localFile2 = new LocalFile(new File("CDE"), "key/cde");
		LocalFile localFile3 = new LocalFile(new File("BCD"), "key/bcd");
		LocalFile localFile4 = new LocalFile(new File("DEF"), "key/def");

		when(existingFileScanner.scan(existingSheet)).thenReturn(existingFiles);
		
		when(sourceFileScanner.scan(sourceSheet)).thenReturn(Arrays.asList(localFile1, localFile2, localFile3, localFile4));
		
		doThrow(IOException.class).when(bucket).upload(localFile2);
		
		ctx.importNewFiles();
		
		verify(bucket).upload(localFile2);
		verify(bucket).upload(localFile3);

		verify(bucket).verifyUpstream(localFile3);

		verifyNoMoreInteractions(bucket);
		
		verify(uploadReport).begin();
		verify(uploadReport).fileUploaded(localFile3);
		verify(uploadReport).finish();
		
		verifyNoMoreInteractions(uploadReport);
	}
	

	@Test
	public void testImportNewFilesReportExceptions() throws Exception {
		CrossReferenceImportAppContext ctx = new CrossReferenceImportAppContext(
				bucket, existingFileScanner, sourceFileScanner, existingSheet,
				sourceSheet, uploadReport);
		
		List<String> existingFiles = Arrays.asList("ABC", "DEF", "GHI");

		LocalFile localFile1 = new LocalFile(new File("ABC"), "key/abc");
		LocalFile localFile2 = new LocalFile(new File("CDE"), "key/cde");
		LocalFile localFile3 = new LocalFile(new File("BCD"), "key/bcd");
		LocalFile localFile4 = new LocalFile(new File("DEF"), "key/def");

		when(existingFileScanner.scan(existingSheet)).thenReturn(existingFiles);
		
		when(sourceFileScanner.scan(sourceSheet)).thenReturn(Arrays.asList(localFile1, localFile2, localFile3, localFile4));
		
		doThrow(IOException.class).when(uploadReport).begin();
		doThrow(IOException.class).when(uploadReport).fileUploaded(localFile2);
		doThrow(IOException.class).when(uploadReport).fileUploaded(localFile3);
		doThrow(IOException.class).when(uploadReport).finish();
		
		ctx.importNewFiles();
		
		verify(bucket).upload(localFile2);
		verify(bucket).upload(localFile3);

		verify(bucket).verifyUpstream(localFile2);
		verify(bucket).verifyUpstream(localFile3);

		verifyNoMoreInteractions(bucket);
		
		verify(uploadReport).begin();
		verify(uploadReport).fileUploaded(localFile2);
		verify(uploadReport).fileUploaded(localFile3);
		verify(uploadReport).finish();
		
		verifyNoMoreInteractions(uploadReport);
	}
}
