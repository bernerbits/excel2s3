package net.bernerbits.client.avolve.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestFailedFile {

	@Test
	public void testGetUpstreamKey()
	{
		FailedFile failedFile = new FailedFile("testfolder", null, new File("test.pdf"));
		assertEquals("testfolder/test.pdf/test.pdf", failedFile.getUpstreamKey());
	}
	
	@Test
	public void testGetLocalMD5() throws IOException
	{
		File tempFile = File.createTempFile("test", ".pdf");
		try(Writer output = new FileWriter(tempFile)) {
			output.write("Hello, world!");
		}
		FailedFile failedFile = new FailedFile(null, null, tempFile);
		assertEquals("6cd3556deb0da54bca060b4c39479839", failedFile.getLocalMD5());
	}
	
	@Test
	public void testGetLocalMD5Fail() throws IOException
	{
		File missingFile = new File("missing.file");
		FailedFile failedFile = new FailedFile(null, null, missingFile);
		assertEquals(null, failedFile.getLocalMD5());
	}
}