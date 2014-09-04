package net.bernerbits.client.avolve.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	private final boolean projectIdEnabled;

	public FailedFolderSpreadsheet(Sheet sheet, DataFormatter df,
			FormulaEvaluator eval, boolean projectIdEnabled) {
		this.sheet = sheet;
		this.df = df;
		this.eval = eval;
		this.projectIdEnabled = projectIdEnabled;
	}

	public List<FailedFolder> readFailedFolders() {
		List<FailedFolder> failedFolders = new ArrayList<>();
		for (Row currentRow : sheet) {
			if (currentRow.getRowNum() == sheet.getTopRow()) {
				continue;
			}
			failedFolders.add(readFailedFolderFromRow(currentRow));
		}
		return failedFolders;
	}

	private FailedFolder readFailedFolderFromRow(Row currentRow) {
		String sourceFolderPath = df.formatCellValue(currentRow.getCell(0),
				eval);
		String failedFolder = df.formatCellValue(currentRow.getCell(1), eval);

		File sourceFolder;
		try {
			sourceFolder = new File(sourceFolderPath).getCanonicalFile();
		} catch (IOException e) {
			errlog.fatal("Could not construct folder: " + sourceFolderPath);
			e.printStackTrace();
			System.exit(1);
			return null;
		}

		File baseFolder;
		if (projectIdEnabled) {
			String projectId = df.formatCellValue(currentRow.getCell(2), eval);
			baseFolder = new File(sourceFolder, projectId);
		} else {
			baseFolder = sourceFolder;
		}
		File folderToSearch = new File(baseFolder, failedFolder);

		return new FailedFolder(folderToSearch, failedFolder);
	}

	/* package */void setLogger(Logger logger) {
		this.errlog = logger;
	}

}
