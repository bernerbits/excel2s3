package net.bernerbits.client.avolve.model;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class FailedFolderSpreadsheet {

	private Logger errlog = Logger.getLogger("errlog");

	private final Sheet sheet;
	private final DataFormatter df;
	private final FormulaEvaluator eval;

	public FailedFolderSpreadsheet(Sheet sheet, DataFormatter df,
			FormulaEvaluator eval) {
		this.sheet = sheet;
		this.df = df;
		this.eval = eval;
	}

	public List<FailedFile> readFailedFiles() {
		List<FailedFile> failedFiles = new ArrayList<>();
		for (Row currentRow : sheet) {
			if (currentRow.getRowNum() == sheet.getTopRow()) {
				continue;
			}
			failedFiles.addAll(readFailedFilesFromRow(currentRow));
		}
		return failedFiles;
	}

	private List<FailedFile> readFailedFilesFromRow(Row currentRow) {
		String sourceFolder = df.formatCellValue(currentRow.getCell(0), eval);
		String failedFolder = df.formatCellValue(currentRow.getCell(1), eval);
		String projectId = df.formatCellValue(currentRow.getCell(2), eval);

		String fileNamePattern = df
				.formatCellValue(currentRow.getCell(3), eval);

		File folderToSearch = new File(new File(new File(sourceFolder),
				projectId), failedFolder);
		try {
			System.out.println("Searching folder: "
					+ folderToSearch.getCanonicalPath());
		} catch (IOException e) {
			errlog.fatal("Could not construct folder: " + sourceFolder + ", "
					+ failedFolder + ", " + projectId);
			e.printStackTrace();
			System.exit(1);
			return null;
		}

		return searchFolder(folderToSearch, failedFolder, fileNamePattern,
				projectId);
	}

	private List<FailedFile> searchFolder(File folderToSearch,
			String failedFolder, String fileNamePattern, String projectId) {
		List<FailedFile> failedFiles = new ArrayList<>();

		if (!fileNamePattern.contains("xxx")) {
			errlog.warn("WARNING! File Name Pattern \"" + fileNamePattern
					+ "\" does not contain a wildcard indicator (\"xxx\").");
		}
		final Pattern fileNameMatcher = Pattern.compile(fileNamePattern
				.replace(".", "\\.").replace("xxx", ".+"),
				Pattern.CASE_INSENSITIVE);

		File[] folders = folderToSearch.listFiles(new FileFilter() {

			@Override
			public boolean accept(File file) {
				return file.isDirectory()
						&& fileNameMatcher.matcher(file.getName()).matches();
			}

		});

		for (File folder : folders) {
			File[] files = folder.listFiles(new FilenameFilter() {

				@Override
				public boolean accept(File dir, String name) {
					return dir.getName().equalsIgnoreCase(name);
				}

			});
			for (File fileToReplace : files) {
				try {
					failedFiles.add(new FailedFile(failedFolder, projectId,
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

	/*package*/ void setLogger(Logger logger) {
		this.errlog = logger;
	}

}
