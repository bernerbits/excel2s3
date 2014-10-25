package net.bernerbits.client.avolve.app.failedfolder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Arrays;

import net.bernerbits.client.avolve.model.Bucket;
import net.bernerbits.client.avolve.model.LocalFile;
import net.bernerbits.client.avolve.model.failedfolder.FailedFolderScanner;
import net.bernerbits.client.avolve.model.sheet.SpreadsheetScanner;

import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TestFailedFolderAppContext {

	@Mock
	private Bucket bucket;
	@Mock
	private Sheet sheet;
	@Mock
	private SpreadsheetScanner<File> sheetScanner;
	@Mock
	private FailedFolderScanner failedFolderScanner;

	@Test
	public void testReplaceFailedFiles() {
		File failedFolder1 = mock(File.class);
		File failedFolder2 = mock(File.class);

		LocalFile failedFile1 = mock(LocalFile.class);
		LocalFile failedFile2 = mock(LocalFile.class);
		LocalFile failedFile3 = mock(LocalFile.class);

		when(sheetScanner.scan(sheet)).thenReturn(
				Arrays.asList(failedFolder1, failedFolder2));

		when(failedFolderScanner.scanForFiles(failedFolder1)).thenReturn(
				Arrays.asList(failedFile1, failedFile2));
		when(failedFolderScanner.scanForFiles(failedFolder2)).thenReturn(
				Arrays.asList(failedFile3));

		FailedFolderAppContext appContext = new FailedFolderAppContext(sheet,
				sheetScanner, bucket, failedFolderScanner);
		appContext.replaceFailedFiles();

		verify(bucket).upload(failedFile1);
		verify(bucket).verifyUpstream(failedFile1);
		verify(bucket).upload(failedFile2);
		verify(bucket).verifyUpstream(failedFile2);
		verify(bucket).upload(failedFile3);
		verify(bucket).verifyUpstream(failedFile3);
	}
}
