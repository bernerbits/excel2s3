package net.bernerbits.client.avolve.app;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import net.bernerbits.client.avolve.model.Bucket;
import net.bernerbits.client.avolve.model.FolderScanner;
import net.bernerbits.client.avolve.model.failedfolder.FailedFolderMapper;

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

public abstract class Config<A extends AppContext> {

	protected static final Logger errlog = Logger.getLogger("errlog");

	protected static final String PROPNAME_SPREADSHEET_PATH = "spreadsheet.path";
	protected static final String PROPNAME_PROJECT_ID_ENABLED = "project.id.enabled";
	protected static final String PROPNAME_AWS_BUCKET_NAME = "aws.bucket.name";
	protected static final String PROPNAME_AWS_KEY_SECRET = "aws.keys.secret";
	protected static final String PROPNAME_AWS_KEY_ACCESS = "aws.keys.access";

	protected Bucket bucket;
	
	protected Config() {
	}

	protected static Bucket loadS3Client(Properties properties) {
		AWSCredentials creds = new BasicAWSCredentials(
				properties.getProperty(PROPNAME_AWS_KEY_ACCESS),
				properties.getProperty(PROPNAME_AWS_KEY_SECRET));
		AWSCredentialsProvider provider = new StaticCredentialsProvider(creds);
		AmazonS3Client s3client = new AmazonS3Client(provider);

		return new Bucket(s3client,
				properties.getProperty(PROPNAME_AWS_BUCKET_NAME));
	}

	protected static Sheet loadSpreadsheet(Properties properties) {
		String sourceSheetName = properties
				.getProperty(PROPNAME_SPREADSHEET_PATH);

		Workbook wb;
		try (final InputStream raw = new FileInputStream(sourceSheetName);
				final InputStream input = new BufferedInputStream(raw)) {
			wb = new XSSFWorkbook(input);
		} catch (FileNotFoundException e) {
			System.err
					.println("Spreadsheet " + sourceSheetName + " not found!");
			System.exit(1);
			return null;
		} catch (IOException e) {
			System.err.println("Error reading file " + sourceSheetName + ": "
					+ e.getLocalizedMessage());
			e.printStackTrace();
			System.exit(1);
			return null;
		}

		Sheet s = wb.getSheetAt(0);
		if (!s.getSheetName().equals("Failed Folders")) {
			errlog.warn("Warning! Unrecognized spreadsheet \""
					+ s.getSheetName()
					+ "\" found (expected: \"Failed Folders\")");
		}
		
		return s;
	}

	public abstract A getAppContext();

}
