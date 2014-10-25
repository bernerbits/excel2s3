package net.bernerbits.client.avolve.model.failedfolder;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import net.bernerbits.client.avolve.model.RowMapperTest;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TestFailedFolderMapper extends RowMapperTest {

	@Mock
	private Logger logger;

	@Test
	public void testScanRow() throws IOException {
		Row dataRow = mockDataRow(1, "./test", "197252", "1");

		FailedFolderMapper mapper = new FailedFolderMapper(df, eval, false);

		File failedFolder = mapper.scanRow(dataRow);

		assertEquals(new File("test/197252").getCanonicalFile(), failedFolder);
	}

	@Test
	public void testScanRowProjectIdEnabled() throws IOException {
		Row dataRow = mockDataRow(1, "./test", "197252", "1");

		FailedFolderMapper mapper = new FailedFolderMapper(df, eval, true);

		File failedFolder = mapper.scanRow(dataRow);

		assertEquals(new File("test/1/197252").getCanonicalFile(), failedFolder);
	}

}
