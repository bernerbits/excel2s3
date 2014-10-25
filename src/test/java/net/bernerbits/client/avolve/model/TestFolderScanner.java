package net.bernerbits.client.avolve.model;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.bernerbits.client.avolve.model.failedfolder.FailedFolder;

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

		File actualLocalFolder = new File("test/197252").getCanonicalFile();
		FailedFolder LocalFolder = new FailedFolder(actualLocalFolder, "197252");

		List<LocalFile> localFiles = folderScanner.scanForFiles(LocalFolder);

		assertEquals(4, localFiles.size());

		LocalFile f1 = localFiles.get(0);
		assertEquals(actualLocalFolder, f1.getLocalFolder());
		assertEquals("197252", f1.getLocalFolderName());
		assertEquals("197252/test_5dbef2a.pdf/test_5dbef2a_v1.pdf",
				f1.getUpstreamKey());
		assertEquals(
				new File("test/197252/test_5dbef2a.pdf/test_5dbef2a_v1.pdf")
						.getCanonicalFile(),
				f1.getLocalFile());

		LocalFile f2 = localFiles.get(1);
		assertEquals(actualLocalFolder, f2.getLocalFolder());
		assertEquals("197252", f2.getLocalFolderName());
		assertEquals("197252/test_5dbef2a.pdf/test_5dbef2a_v2.pdf",
				f2.getUpstreamKey());
		assertEquals(
				new File("test/197252/test_5dbef2a.pdf/test_5dbef2a_v2.pdf")
						.getCanonicalFile(),
				f2.getLocalFile());

		LocalFile f3 = localFiles.get(2);
		assertEquals(actualLocalFolder, f3.getLocalFolder());
		assertEquals("197252", f3.getLocalFolderName());
		assertEquals("197252/test_5dbef2a.pdf/test_5dbef2a_v3.pdf",
				f3.getUpstreamKey());
		assertEquals(
				new File("test/197252/test_5dbef2a.pdf/test_5dbef2a_v3.pdf")
						.getCanonicalFile(),
				f3.getLocalFile());

		LocalFile f4 = localFiles.get(3);
		assertEquals(actualLocalFolder, f4.getLocalFolder());
		assertEquals("197252", f4.getLocalFolderName());
		assertEquals("197252/test_9b70a6c.pdf/test_9b70a6c.pdf",
				f4.getUpstreamKey());
		assertEquals(
				new File("test/197252/test_9b70a6c.pdf/test_9b70a6c.pdf")
						.getCanonicalFile(),
				f4.getLocalFile());

		verify(logger, never()).warn(any());
	}

}
