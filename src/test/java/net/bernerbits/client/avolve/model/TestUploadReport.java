package net.bernerbits.client.avolve.model;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;

import org.junit.Test;

import com.amazonaws.util.IOUtils;

public class TestUploadReport {
	@Test
	public void testUploadReport() throws Exception {
		File tempFile = File.createTempFile("test", ".txt");
		UploadReport report = new UploadReport(tempFile.getCanonicalPath());

		report.begin();
		report.fileUploaded(new LocalFile(new File("ABC"), "key/abc"));
		report.fileUploaded(new LocalFile(new File("DEF"), "key/def"));
		report.fileUploaded(new LocalFile(new File("GHI"), "key/ghi"));
		report.finish();

		String fileContents = IOUtils.toString(new FileInputStream(tempFile));

		assertEquals("ABC -> key/abc\n" 
				+ "DEF -> key/def\n"
				+ "GHI -> key/ghi\n"
				+ "Files uploaded: 3", fileContents);
	}
}
