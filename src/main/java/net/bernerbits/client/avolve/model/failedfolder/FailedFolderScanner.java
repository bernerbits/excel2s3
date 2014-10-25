package net.bernerbits.client.avolve.model.failedfolder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.bernerbits.client.avolve.model.BaseFolderScanner;
import net.bernerbits.client.avolve.model.LocalFile;

import org.apache.log4j.Logger;

public class FailedFolderScanner extends BaseFolderScanner<File> {
	private Logger errlog = Logger.getLogger("errlog");

	public List<LocalFile> scanForFiles(File failedFolder) {
		System.out.println("Searching folder: " + failedFolder.getPath());

		List<LocalFile> failedFiles = new ArrayList<>();

		List<File> allFiles = recursiveScan(failedFolder);

		for (File fileToReplace : allFiles) {
			try {
				String upstreamKey = failedFolder.getName()
						+ fileToReplace.getPath()
								.replace(failedFolder.getPath(), "")
								.replace('\\', '/');
				failedFiles.add(new LocalFile(fileToReplace.getCanonicalFile(),
						upstreamKey));
			} catch (IOException e) {
				errlog.warn("WARNING! Could not resolve file: "
						+ fileToReplace.getPath()
						+ ". File will not be replaced.");
				e.printStackTrace();
			}
		}

		return failedFiles;
	}

	/* package */void setLogger(Logger logger) {
		this.errlog = logger;
	}
}
