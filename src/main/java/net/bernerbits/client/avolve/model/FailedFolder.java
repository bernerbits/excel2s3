package net.bernerbits.client.avolve.model;

import java.io.File;

public class FailedFolder {

	private final File folderToSearch;
	private final String failedFolder;
	private final String fileNamePattern;
	private final String projectId;

	public FailedFolder(File folderToSearch, String failedFolder,
			String fileNamePattern, String projectId) {
		this.folderToSearch = folderToSearch;
		this.failedFolder = failedFolder;
		this.fileNamePattern = fileNamePattern;
		this.projectId = projectId;
	}

	public File getFolderToSearch() {
		return folderToSearch;
	}

	public String getFailedFolder() {
		return failedFolder;
	}

	public String getFileNamePattern() {
		return fileNamePattern;
	}

	public String getProjectId() {
		return projectId;
	}
}
