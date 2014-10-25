package net.bernerbits.client.avolve.model.failedfolder;

import java.io.File;
import java.io.IOException;

import net.bernerbits.client.avolve.model.sheet.IRowMapper;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;

public class FailedFolderMapper implements IRowMapper<File> {

	private Logger errlog = Logger.getLogger("errlog");

	private final DataFormatter df;
	private final FormulaEvaluator eval;
	private final boolean projectIdEnabled;

	public FailedFolderMapper(DataFormatter df, FormulaEvaluator eval,
			boolean projectIdEnabled) {
		super();
		this.df = df;
		this.eval = eval;
		this.projectIdEnabled = projectIdEnabled;
	}

	@Override
	public File scanRow(Row row) {
		String sourceFolderPath = df.formatCellValue(row.getCell(0), eval);
		String failedFolder = df.formatCellValue(row.getCell(1), eval);

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
			String projectId = df.formatCellValue(row.getCell(2), eval);
			baseFolder = new File(sourceFolder, projectId);
		} else {
			baseFolder = sourceFolder;
		}
		return new File(baseFolder, failedFolder);
	}

	/* package */void setLogger(Logger logger) {
		this.errlog = logger;
	}

}
