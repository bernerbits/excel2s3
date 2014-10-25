package net.bernerbits.client.avolve.model.failedfolder;

import java.io.File;

public class FailedFolder {

	private final File folderToSearch;
	private final String failedFolder;

	public FailedFolder(File folderToSearch, String failedFolder) {
		this.folderToSearch = folderToSearch;
		this.failedFolder = failedFolder;
	}

	public File getFolderToSearch() {
		return folderToSearch;
	}

	public String getFailedFolder() {
		return failedFolder;
	}

}
