package net.bernerbits.client.avolve.app;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.bernerbits.client.avolve.model.Buckets;
import net.bernerbits.client.avolve.model.FailedFolderSpreadsheet;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.internal.StaticCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3Client;

public class Config {

	private static final Logger errlog = Logger.getLogger("errlog");
	
	private static final String PROPNAME_SPREADSHEET_PATH = "spreadsheet.path";
	private static final String PROPNAME_PROJECT_BUCKET_PREFIX = "project.bucket.";
	private static final String PROPNAME_AWS_KEY_SECRET = "aws.key.secret";
	private static final String PROPNAME_AWS_KEY_ACCESS = "aws.key.access";
	
	private Buckets buckets;
	private FailedFolderSpreadsheet spreadsheet;

	private Config() {
	}

	public static Config loadConfig(String propertiesPath) {
		
		Config config = new Config();
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

		loadSpreadsheet(config, properties);
		loadS3Client(config, properties);

		return config;
	}

	private static void loadS3Client(Config config, Properties properties) {
		AWSCredentials creds = new BasicAWSCredentials(
				properties.getProperty(PROPNAME_AWS_KEY_ACCESS),
				properties.getProperty(PROPNAME_AWS_KEY_SECRET));
		AWSCredentialsProvider provider = new StaticCredentialsProvider(creds);
		AmazonS3Client s3client = new AmazonS3Client(provider);

		Map<String, String> projectIdToBucketNameMap = new HashMap<>();
		for (String propertyName : properties.stringPropertyNames()) {
			if (propertyName.startsWith(PROPNAME_PROJECT_BUCKET_PREFIX)) {
				String projectId = propertyName.substring(15);
				String bucketName = properties.getProperty(propertyName);
				projectIdToBucketNameMap.put(projectId, bucketName);
			}
		}
		config.buckets = new Buckets(s3client, projectIdToBucketNameMap);
	}

	private static void loadSpreadsheet(Config config, Properties properties) {
		String sourceSheetName = properties.getProperty(PROPNAME_SPREADSHEET_PATH);

		Workbook wb;
		try (final InputStream raw = new FileInputStream(sourceSheetName);
				final InputStream input = new BufferedInputStream(raw)) {
			wb = new XSSFWorkbook(input);
		} catch (FileNotFoundException e) {
			System.err
					.println("Spreadsheet " + sourceSheetName + " not found!");
			System.exit(1);
			return;
		} catch (IOException e) {
			System.err.println("Error reading file " + sourceSheetName + ": "
					+ e.getLocalizedMessage());
			e.printStackTrace();
			System.exit(1);
			return;
		}

		Sheet s = wb.getSheetAt(0);
		if (!s.getSheetName().equals("Failed Folders")) {
			errlog.warn("Warning! Unrecognized spreadsheet \""
					+ s.getSheetName()
					+ "\" found (expected: \"Failed Folders\")");
		}

		DataFormatter df = new DataFormatter();
		FormulaEvaluator eval = new XSSFFormulaEvaluator((XSSFWorkbook) wb);

		config.spreadsheet = new FailedFolderSpreadsheet(s, df, eval);
	}

	public AppContext getAppContext() {
		return new AppContext(spreadsheet, buckets);
	}

}
