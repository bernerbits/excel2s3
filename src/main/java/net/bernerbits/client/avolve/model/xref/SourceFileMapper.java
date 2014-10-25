package net.bernerbits.client.avolve.model.xref;

import java.io.File;
import java.io.IOException;

import net.bernerbits.client.avolve.model.LocalFile;
import net.bernerbits.client.avolve.model.sheet.IRowMapper;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;

public class SourceFileMapper implements IRowMapper<LocalFile> {

	private Logger errlog = Logger.getLogger("errlog");

	private final DataFormatter df;
	private final FormulaEvaluator eval;

	private final String networkRoot;
	private final File mappedRoot;

	public SourceFileMapper(DataFormatter df, FormulaEvaluator eval,
			String networkRoot, File mappedRoot) {
		this.df = df;
		this.eval = eval;
		this.networkRoot = networkRoot;
		this.mappedRoot = mappedRoot;
	}

	@Override
	public LocalFile scanRow(Row row) {
		String remoteFolderId = df.formatCellValue(row.getCell(0), eval);
		String localPath = df.formatCellValue(row.getCell(10), eval);

		if (localPath.isEmpty()) {
			errlog.warn("Row # " + row.getRowNum() + " could not be read");
			return null;
		}

		if (!localPath.toUpperCase().startsWith(networkRoot.toUpperCase())) {
			String errMsg = "Invalid folder location: " + localPath
					+ ". Must start with network root (" + networkRoot + ")";
			errlog.fatal(errMsg);
			System.err.println(errMsg);
			System.exit(1);
			return null;
		}

		localPath = mappedRoot.getPath() + "/"
				+ localPath.substring(networkRoot.length()).replace('\\', '/');
		File localFile = new File(localPath);
		if (!localFile.getName().matches(".*_V\\d+$")) {
			localFile = new File(localFile, localFile.getName());
		}

		try {
			localFile = localFile.getCanonicalFile();
		} catch (IOException ex) {
			errlog.fatal("Invalid file: " + localFile, ex);
			ex.printStackTrace(System.err);
			System.exit(1);
			return null;
		}

		return new LocalFile(localFile, remoteFolderId + "/"
				+ localFile.getParentFile().getName() + "/"
				+ localFile.getName());
	}

	/* package */void setLogger(Logger logger) {
		this.errlog = logger;
	}

}
