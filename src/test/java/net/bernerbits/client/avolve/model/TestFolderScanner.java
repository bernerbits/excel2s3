package net.bernerbits.client.avolve.model;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TestFolderScanner {
	@Mock
	private Logger logger;

	@Test
	public void testScanForFiles() throws IOException {
		FolderScanner folderScanner = new FolderScanner();
		folderScanner.setLogger(logger);

		File actualFailedFolder = new File("test/197252").getCanonicalFile();
		FailedFolder failedFolder = new FailedFolder(actualFailedFolder,
				"197252");

		List<FailedFile> failedFiles = folderScanner.scanForFiles(failedFolder);

		assertEquals(4, failedFiles.size());

		FailedFile f1 = failedFiles.get(0);
		assertEquals(actualFailedFolder, f1.getFailedFolder());
		assertEquals("197252", f1.getFailedFolderName());
		assertEquals("197252/test_5dbef2a.pdf/test_5dbef2a_v1.pdf",
				f1.getUpstreamKey());
		assertEquals(
				new File("test/197252/test_5dbef2a.pdf/test_5dbef2a_v1.pdf")
						.getCanonicalFile(),
				f1.getFileToReplace());
		
		FailedFile f2 = failedFiles.get(1);
		assertEquals(actualFailedFolder, f2.getFailedFolder());
		assertEquals("197252", f2.getFailedFolderName());
		assertEquals("197252/test_5dbef2a.pdf/test_5dbef2a_v2.pdf",
				f2.getUpstreamKey());
		assertEquals(
				new File("test/197252/test_5dbef2a.pdf/test_5dbef2a_v2.pdf")
						.getCanonicalFile(),
				f2.getFileToReplace());

		FailedFile f3 = failedFiles.get(2);
		assertEquals(actualFailedFolder, f3.getFailedFolder());
		assertEquals("197252", f3.getFailedFolderName());
		assertEquals("197252/test_5dbef2a.pdf/test_5dbef2a_v3.pdf",
				f3.getUpstreamKey());
		assertEquals(
				new File("test/197252/test_5dbef2a.pdf/test_5dbef2a_v3.pdf")
						.getCanonicalFile(),
				f3.getFileToReplace());

		FailedFile f4 = failedFiles.get(3);
		assertEquals(actualFailedFolder, f4.getFailedFolder());
		assertEquals("197252", f4.getFailedFolderName());
		assertEquals("197252/test_9b70a6c.pdf/test_9b70a6c.pdf",
				f4.getUpstreamKey());
		assertEquals(
				new File("test/197252/test_9b70a6c.pdf/test_9b70a6c.pdf")
						.getCanonicalFile(),
				f4.getFileToReplace());

		verify(logger, never()).warn(any());
	}

}
