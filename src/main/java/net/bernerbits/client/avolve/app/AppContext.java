package net.bernerbits.client.avolve.app;

import net.bernerbits.client.avolve.model.Bucket;
import net.bernerbits.client.avolve.model.Buckets;
import net.bernerbits.client.avolve.model.FailedFile;
import net.bernerbits.client.avolve.model.FailedFolderSpreadsheet;

public class AppContext {

	private final FailedFolderSpreadsheet spreadsheet;
	private final Buckets buckets;
	
	public AppContext(FailedFolderSpreadsheet spreadsheet, Buckets buckets) {
		this.spreadsheet = spreadsheet;
		this.buckets = buckets;
	}

	public void replaceFailedFiles() {
		for(FailedFile failedFile: spreadsheet.readFailedFiles())
		{
			Bucket bucket = buckets.getBucket(failedFile.getProjectId());
			bucket.upload(failedFile);
			bucket.verifyUpstream(failedFile);
		}
		System.out.println("All files uploaded! Please check errors.log for problems.");
	}

}
