package net.bernerbits.client.avolve.model;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class FolderScanner {
	private Logger errlog = Logger.getLogger("errlog");

	public List<FailedFile> scanForFiles(FailedFolder failedFolder) {
		System.out.println("Searching folder: " + failedFolder.getFolderToSearch().getPath());

		List<FailedFile> failedFiles = new ArrayList<>();

		File[] fileSubfolders = scanForFileSubfolders(failedFolder.getFolderToSearch(),
				compileRegex(failedFolder.getFileNamePattern()));

		for (File folder : fileSubfolders) {
			File fileToReplace = getFileMatchingSubfolderName(folder);
			if (fileToReplace != null)
			{
				try {
					failedFiles.add(new FailedFile(failedFolder.getFailedFolder(), 
							fileToReplace.getCanonicalFile()));
				} catch (IOException e) {
					errlog.warn("WARNING! Could not resolve file: "
							+ fileToReplace.getPath()
							+ ". File will not be replaced.");
					e.printStackTrace();
				}
			}
		}

		return failedFiles;
	}

	private File getFileMatchingSubfolderName(File folder) {
		File file = new File(folder, folder.getName());
		if (file.exists()) {
			return file;
		} else {
			return null;
		}
	}

	private File[] scanForFileSubfolders(File folderToSearch,
			final Pattern fileNameRegex) {
		return folderToSearch.listFiles(new FileFilter() {

			@Override
			public boolean accept(File file) {
				return file.isDirectory()
						&& fileNameRegex.matcher(file.getName()).matches();
			}

		});
	}

	private Pattern compileRegex(String fileNamePattern) {
		if (!fileNamePattern.contains("xxx")) {
			errlog.warn("WARNING! File Name Pattern \"" + fileNamePattern
					+ "\" does not contain a wildcard indicator (\"xxx\").");
		}
		final Pattern fileNameMatcher = Pattern.compile(fileNamePattern
				.replace(".", "\\.").replace("xxx", ".+"),
				Pattern.CASE_INSENSITIVE);
		return fileNameMatcher;
	}

	/* package */void setLogger(Logger logger) {
		this.errlog = logger;
	}
}
