package net.bernerbits.client.avolve.app;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import net.bernerbits.client.avolve.model.Bucket;
import net.bernerbits.client.avolve.model.FailedFile;
import net.bernerbits.client.avolve.model.FailedFolder;
import net.bernerbits.client.avolve.model.FailedFolderSpreadsheet;
import net.bernerbits.client.avolve.model.FolderScanner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TestAppContext {

	@Mock
	private Bucket bucket;
	@Mock
	private FailedFolderSpreadsheet spreadsheet;
	@Mock
	private FolderScanner folderScanner;

	@Test
	public void testReplaceFailedFiles() {
		FailedFolder failedFolder1 = mock(FailedFolder.class);
		FailedFolder failedFolder2 = mock(FailedFolder.class);

		FailedFile failedFile1 = mock(FailedFile.class);
		FailedFile failedFile2 = mock(FailedFile.class);
		FailedFile failedFile3 = mock(FailedFile.class);

		when(spreadsheet.readFailedFolders()).thenReturn(
				Arrays.asList(failedFolder1, failedFolder2));

		when(folderScanner.scanForFiles(failedFolder1)).thenReturn(
				Arrays.asList(failedFile1, failedFile2));
		when(folderScanner.scanForFiles(failedFolder2)).thenReturn(
				Arrays.asList(failedFile3));

		AppContext appContext = new AppContext(spreadsheet, bucket,
				folderScanner);
		appContext.replaceFailedFiles();

		verify(bucket).upload(failedFile1);
		verify(bucket).verifyUpstream(failedFile1);
		verify(bucket).upload(failedFile2);
		verify(bucket).verifyUpstream(failedFile2);
		verify(bucket).upload(failedFile3);
		verify(bucket).verifyUpstream(failedFile3);
	}
}
