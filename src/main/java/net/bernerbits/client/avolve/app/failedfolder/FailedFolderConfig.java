package net.bernerbits.client.avolve.app.failedfolder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import net.bernerbits.client.avolve.app.Config;
import net.bernerbits.client.avolve.model.FolderScanner;
import net.bernerbits.client.avolve.model.failedfolder.FailedFolder;
import net.bernerbits.client.avolve.model.failedfolder.FailedFolderMapper;
import net.bernerbits.client.avolve.model.sheet.SpreadsheetScanner;

import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FailedFolderConfig extends Config<FailedFolderAppContext> {

	private Sheet sheet;
	private FailedFolderMapper folderMapper;

	private static FailedFolderMapper getFailedFolderMapper(Sheet sheet,
			Properties properties) {
		boolean projectIdEnabled = Boolean.valueOf(properties
				.getProperty(PROPNAME_PROJECT_ID_ENABLED));

		DataFormatter df = new DataFormatter();
		Workbook wb = sheet.getWorkbook();
		FormulaEvaluator eval;
		if (wb instanceof XSSFWorkbook) {
			eval = new XSSFFormulaEvaluator((XSSFWorkbook) sheet.getWorkbook());
		} else {
			eval = new HSSFFormulaEvaluator((HSSFWorkbook) sheet.getWorkbook());
		}

		return new FailedFolderMapper(df, eval, projectIdEnabled);
	}

	public static FailedFolderConfig loadConfig(String propertiesPath) {

		FailedFolderConfig config = new FailedFolderConfig();
		Properties properties = new Properties();
		try (InputStream input = new FileInputStream(propertiesPath)) {
			properties.load(input);
		} catch (FileNotFoundException e) {
			System.err.println("Configuration file " + propertiesPath
					+ " not found!");
			System.exit(1);
			return null;
		} catch (IOException e) {
			System.err.println("Error loading configuration file "
					+ propertiesPath + ": " + e.getMessage());
			e.printStackTrace();
			System.exit(1);
			return null;
		}

		config.sheet = loadSpreadsheet(properties);
		config.bucket = loadS3Client(properties);
		config.folderMapper = getFailedFolderMapper(config.sheet, properties);

		return config;
	}

	@Override
	public FailedFolderAppContext getAppContext() {
		return new FailedFolderAppContext(sheet,
				new SpreadsheetScanner<FailedFolder>(folderMapper), bucket,
				new FolderScanner());
	}
}
