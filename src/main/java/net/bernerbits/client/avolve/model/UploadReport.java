package net.bernerbits.client.avolve.model;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

public class UploadReport {

	private final String reportFile;
	
	private BufferedOutputStream output;

	private int count = 0;

	public UploadReport(String reportFile) {
		this.reportFile = reportFile;
	}

	public synchronized void fileUploaded(LocalFile localFile) throws IOException {
		count++;
		output.write((localFile.getLocalFile() + " -> "
				+ localFile.getUpstreamKey() + "\n").getBytes(Charset
				.forName("US-ASCII")));
	}

	public synchronized void finish() throws IOException {
		output.write(("Files uploaded: " + count).getBytes(Charset
				.forName("US-ASCII")));
		output.flush();
		output.close();
		System.out.println("Upload report is available: " + reportFile);
	}

	public void begin() throws FileNotFoundException {
		output = new BufferedOutputStream(new FileOutputStream(reportFile));
	}

}
