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
		String sourceFolderPath = df.formatCellValue(currentRow.getCell(0), eval);
		String failedFolder = df.formatCellValue(currentRow.getCell(1), eval);
		String projectId = df.formatCellValue(currentRow.getCell(2), eval);
		String fileNamePattern = df
				.formatCellValue(currentRow.getCell(3), eval);

		File sourceFolder;
		try {
			sourceFolder = new File(sourceFolderPath).getCanonicalFile();
		} catch (IOException e) {
			errlog.fatal("Could not construct folder: " + sourceFolderPath);
			e.printStackTrace();
			System.exit(1);
			return null;
		}
		File folderToSearch = new File(sourceFolder, failedFolder);
		
		return new FailedFolder(folderToSearch, failedFolder, fileNamePattern,
				projectId);
	}

	/*package*/ void setLogger(Logger logger) {
		this.errlog = logger;
	}

}
