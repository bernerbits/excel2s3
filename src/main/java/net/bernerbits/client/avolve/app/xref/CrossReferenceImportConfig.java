package net.bernerbits.client.avolve.app.xref;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import net.bernerbits.client.avolve.app.Config;
import net.bernerbits.client.avolve.model.sheet.SpreadsheetScanner;
import net.bernerbits.client.avolve.model.xref.ExistingFileMapper;
import net.bernerbits.client.avolve.model.xref.SourceFileMapper;

import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CrossReferenceImportConfig extends
		Config<CrossReferenceImportAppContext> {

	private static final String PROPNAME_NETWORK_ROOT = "root.dir.network";
	private static final String PROPNAME_MAPPED_ROOT = "root.dir.mapped";

	private Sheet existingSheet;
	private Sheet sourceSheet;

	private ExistingFileMapper existingMapper;
	private SourceFileMapper sourceMapper;

	private static ExistingFileMapper getExistingFileMapper(Sheet sheet) {
		DataFormatter df = new DataFormatter();
		Workbook wb = sheet.getWorkbook();
		FormulaEvaluator eval;
		if (wb instanceof XSSFWorkbook) {
			eval = new XSSFFormulaEvaluator((XSSFWorkbook) sheet.getWorkbook());
		} else {
			eval = new HSSFFormulaEvaluator((HSSFWorkbook) sheet.getWorkbook());
		}

		return new ExistingFileMapper(df, eval);
	}

	private static SourceFileMapper getSourceFileMapper(Sheet sheet,
			Properties properties) {
		DataFormatter df = new DataFormatter();
		Workbook wb = sheet.getWorkbook();
		FormulaEvaluator eval;
		if (wb instanceof XSSFWorkbook) {
			eval = new XSSFFormulaEvaluator((XSSFWorkbook) sheet.getWorkbook());
		} else {
			eval = new HSSFFormulaEvaluator((HSSFWorkbook) sheet.getWorkbook());
		}

		return new SourceFileMapper(df, eval,
				properties.getProperty(PROPNAME_NETWORK_ROOT), new File(
						properties.getProperty(PROPNAME_MAPPED_ROOT)));
	}

	public static CrossReferenceImportConfig loadConfig(String propertiesPath) {

		CrossReferenceImportConfig config = new CrossReferenceImportConfig();
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

		config.sourceSheet = loadSpreadsheet(
				properties.getProperty(PROPNAME_SPREADSHEET_PATH), 0);
		config.existingSheet = loadSpreadsheet(
				properties.getProperty(PROPNAME_SPREADSHEET_EXISTING_PATH), 0);

		config.bucket = loadS3Client(properties);

		config.sourceMapper = getSourceFileMapper(config.sourceSheet,
				properties);
		config.existingMapper = getExistingFileMapper(config.existingSheet);

		return config;
	}

	@Override
	public CrossReferenceImportAppContext getAppContext() {
		return new CrossReferenceImportAppContext(bucket, new SpreadsheetScanner<>(existingMapper),
				 new SpreadsheetScanner<>(sourceMapper), existingSheet, sourceSheet);
	}
}
