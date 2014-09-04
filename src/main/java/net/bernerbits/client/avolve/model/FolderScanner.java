package net.bernerbits.client.avolve.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class FolderScanner {
	private Logger errlog = Logger.getLogger("errlog");

	public List<FailedFile> scanForFiles(FailedFolder failedFolder) {
		System.out.println("Searching folder: "
				+ failedFolder.getFolderToSearch().getPath());

		List<FailedFile> failedFiles = new ArrayList<>();

		List<File> allFiles = recursiveScan(failedFolder.getFolderToSearch());

		for (File fileToReplace : allFiles) {
			try {
				failedFiles.add(new FailedFile(failedFolder.getFolderToSearch(),
						fileToReplace.getCanonicalFile()));
			} catch (IOException e) {
				errlog.warn("WARNING! Could not resolve file: "
						+ fileToReplace.getPath()
						+ ". File will not be replaced.");
				e.printStackTrace();
			}
		}

		return failedFiles;
	}

	private List<File> recursiveScan(File folderToSearch) {
		List<File> files = new ArrayList<File>();
		for(File fileInFolder : folderToSearch.listFiles())
		{
			if(fileInFolder.isDirectory())
			{
				files.addAll(recursiveScan(fileInFolder));
			}
			else
			{
				files.add(fileInFolder);
			}
		}
		return files;
	}

	/* package */void setLogger(Logger logger) {
		this.errlog = logger;
	}
}
