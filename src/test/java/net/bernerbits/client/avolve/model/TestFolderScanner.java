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

		FailedFolder failedFolder = new FailedFolder(new File("test/197252"),
				"197252", "xxx.pdf", null);

		List<FailedFile> failedFiles = folderScanner.scanForFiles(failedFolder);

		assertEquals(2, failedFiles.size());

		FailedFile f1 = failedFiles.get(0);
		assertEquals("197252", f1.getFailedFolder());
		assertEquals("197252/test_5dbef2a.pdf/test_5dbef2a.pdf",
				f1.getUpstreamKey());
		assertEquals(
				new File("test/197252/test_5dbef2a.pdf/test_5dbef2a.pdf")
						.getCanonicalFile(),
				f1.getFileToReplace());

		FailedFile f2 = failedFiles.get(1);
		assertEquals("197252", f2.getFailedFolder());
		assertEquals("197252/test_9b70a6c.pdf/test_9b70a6c.pdf",
				f2.getUpstreamKey());
		assertEquals(
				new File("test/197252/test_9b70a6c.pdf/test_9b70a6c.pdf")
						.getCanonicalFile(),
				f2.getFileToReplace());

		verify(logger, never()).warn(any());
	}
	
	@Test
	public void testScanForFilesBadPattern() {
		FolderScanner folderScanner = new FolderScanner();
		folderScanner.setLogger(logger);

		FailedFolder failedFolder = new FailedFolder(new File("test/197252"),
				"197252", "xx.pdf", null);

		List<FailedFile> failedFiles = folderScanner.scanForFiles(failedFolder);

		assertEquals(0, failedFiles.size());
		verify(logger).warn(any());
	}

	@Test
	public void testScanForFilesSkipNonFolderFiles() {
		FolderScanner folderScanner = new FolderScanner();
		folderScanner.setLogger(logger);

		FailedFolder failedFolder = new FailedFolder(new File("test/197252_bad"),
				"197252_bad", "xxx.pdf", null);

		List<FailedFile> failedFiles = folderScanner.scanForFiles(failedFolder);

		assertEquals(0, failedFiles.size());
		verify(logger, never()).warn(any());
	}
}
