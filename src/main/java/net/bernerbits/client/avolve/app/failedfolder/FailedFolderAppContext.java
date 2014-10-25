package net.bernerbits.client.avolve.app.failedfolder;

import net.bernerbits.client.avolve.app.AppContext;
import net.bernerbits.client.avolve.model.Bucket;
import net.bernerbits.client.avolve.model.LocalFile;
import net.bernerbits.client.avolve.model.FolderScanner;
import net.bernerbits.client.avolve.model.failedfolder.FailedFolder;
import net.bernerbits.client.avolve.model.sheet.SpreadsheetScanner;

import org.apache.poi.ss.usermodel.Sheet;

public class FailedFolderAppContext extends AppContext {

	private final Sheet spreadsheet;
	private final SpreadsheetScanner<FailedFolder> spreadsheetScanner;
	private final FolderScanner folderScanner;

	public FailedFolderAppContext(Sheet spreadsheet,
			SpreadsheetScanner<FailedFolder> spreadsheetScanner, Bucket bucket,
			FolderScanner folderScanner) {
		super(bucket);
		this.spreadsheet = spreadsheet;
		this.spreadsheetScanner = spreadsheetScanner;
		this.folderScanner = folderScanner;
	}

	public void replaceFailedFiles() {
		for (FailedFolder failedFolder : spreadsheetScanner.scan(spreadsheet)) {
			for (LocalFile failedFile : folderScanner
					.scanForFiles(failedFolder)) {
				bucket.upload(failedFile);
				bucket.verifyUpstream(failedFile);
			}
		}
		System.out
				.println("All files uploaded! Please check errors.log for problems.");
	}

}
