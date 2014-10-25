package net.bernerbits.client.avolve.app.failedfolder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import net.bernerbits.client.avolve.app.failedfolder.FailedFolderAppContext;
import net.bernerbits.client.avolve.model.Bucket;
import net.bernerbits.client.avolve.model.LocalFile;
import net.bernerbits.client.avolve.model.FolderScanner;
import net.bernerbits.client.avolve.model.failedfolder.FailedFolder;
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
	private SpreadsheetScanner<FailedFolder> sheetScanner;
	@Mock
	private FolderScanner folderScanner;

	@Test
	public void testReplaceFailedFiles() {
		FailedFolder failedFolder1 = mock(FailedFolder.class);
		FailedFolder failedFolder2 = mock(FailedFolder.class);

		LocalFile failedFile1 = mock(LocalFile.class);
		LocalFile failedFile2 = mock(LocalFile.class);
		LocalFile failedFile3 = mock(LocalFile.class);

		when(sheetScanner.scan(sheet)).thenReturn(
				Arrays.asList(failedFolder1, failedFolder2));

		when(folderScanner.scanForFiles(failedFolder1)).thenReturn(
				Arrays.asList(failedFile1, failedFile2));
		when(folderScanner.scanForFiles(failedFolder2)).thenReturn(
				Arrays.asList(failedFile3));

		FailedFolderAppContext appContext = new FailedFolderAppContext(sheet,
				sheetScanner, bucket, folderScanner);
		appContext.replaceFailedFiles();

		verify(bucket).upload(failedFile1);
		verify(bucket).verifyUpstream(failedFile1);
		verify(bucket).upload(failedFile2);
		verify(bucket).verifyUpstream(failedFile2);
		verify(bucket).upload(failedFile3);
		verify(bucket).verifyUpstream(failedFile3);
	}
}
