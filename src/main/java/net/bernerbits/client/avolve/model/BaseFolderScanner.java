package net.bernerbits.client.avolve.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseFolderScanner<T> {

	public abstract List<LocalFile> scanForFiles(T location);
	
	protected List<File> recursiveScan(File folderToSearch) {
		List<File> files = new ArrayList<File>();
		for (File fileInFolder : folderToSearch.listFiles()) {
			if (fileInFolder.isDirectory()) {
				files.addAll(recursiveScan(fileInFolder));
			} else {
				files.add(fileInFolder);
			}
		}
		return files;
	}

}
