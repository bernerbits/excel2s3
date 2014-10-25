package net.bernerbits.client.avolve.model.xref;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import net.bernerbits.client.avolve.model.LocalFile;

import org.junit.Test;

public class TestExistingFileFilter {
	@Test
	public void testFilterExistingFiles() {
		List<String> existingFiles = Arrays.asList("ABC", "DEF", "GHI");

		LocalFile localFile1 = new LocalFile(new File("ABC"), "");
		LocalFile localFile2 = new LocalFile(new File("CDE"), "");
		LocalFile localFile3 = new LocalFile(new File("BCD"), "");
		LocalFile localFile4 = new LocalFile(new File("DEF"), "");

		List<LocalFile> sourceFiles = Arrays.asList(localFile1, localFile2,
				localFile3, localFile4);

		List<LocalFile> filteredFiles = new ExistingFileFilter()
				.filterExistingFiles(existingFiles, sourceFiles);

		assertEquals(2, filteredFiles.size());
		assertTrue(filteredFiles.contains(localFile2));
		assertTrue(filteredFiles.contains(localFile3));
	}
}
