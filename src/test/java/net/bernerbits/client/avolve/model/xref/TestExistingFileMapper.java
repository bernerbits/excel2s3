package net.bernerbits.client.avolve.model.xref;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import net.bernerbits.client.avolve.model.LocalFile;
import net.bernerbits.client.avolve.model.RowMapperTest;

import org.apache.poi.ss.usermodel.Row;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TestExistingFileMapper extends RowMapperTest {
	@Test
	public void testScanRow() throws IOException {
		Row dataRow = mockDataRow(1, null, null, null, null, "File");

		ExistingFileMapper mapper = new ExistingFileMapper(df, eval);

		String existingFile = mapper.scanRow(dataRow);

		assertEquals("File", existingFile);
	}
}
