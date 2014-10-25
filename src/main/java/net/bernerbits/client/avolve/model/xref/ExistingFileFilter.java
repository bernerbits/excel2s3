package net.bernerbits.client.avolve.model.xref;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.bernerbits.client.avolve.model.LocalFile;

public class ExistingFileFilter {
	public List<LocalFile> filterExistingFiles(List<String> existingFiles,
			List<LocalFile> sourceFiles) {
		Set<String> existingFileSet = new HashSet<String>();
		for (String existingFile : existingFiles) {
			existingFileSet.add(existingFile.toUpperCase());
		}

		List<LocalFile> filteredSourceFiles = new ArrayList<LocalFile>();
		for (LocalFile sourceFile : sourceFiles) {
			if (!existingFileSet.contains(sourceFile.getLocalFile().getName()
					.toUpperCase())) {
				filteredSourceFiles.add(sourceFile);
			}
		}

		return filteredSourceFiles;
	}
}
