package net.bernerbits.client.avolve.app;

import net.bernerbits.client.avolve.model.Bucket;
import net.bernerbits.client.avolve.model.FailedFile;
import net.bernerbits.client.avolve.model.FailedFolder;
import net.bernerbits.client.avolve.model.FailedFolderSpreadsheet;
import net.bernerbits.client.avolve.model.FolderScanner;

public class AppContext {

	private final FailedFolderSpreadsheet spreadsheet;
	private final Bucket bucket;
	private final FolderScanner folderScanner;
	
	public AppContext(FailedFolderSpreadsheet spreadsheet, Bucket bucket, FolderScanner folderScanner) {
		this.spreadsheet = spreadsheet;
		this.bucket = bucket;
		this.folderScanner = folderScanner;
	}

	public void replaceFailedFiles() {
		for(FailedFolder failedFolder: spreadsheet.readFailedFolders())
		{
			for(FailedFile failedFile : folderScanner.scanForFiles(failedFolder))
			{
				bucket.upload(failedFile);
				bucket.verifyUpstream(failedFile);
			}
		}
		System.out.println("All files uploaded! Please check errors.log for problems.");
	}

}
