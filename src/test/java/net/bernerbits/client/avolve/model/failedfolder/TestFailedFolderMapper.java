package net.bernerbits.client.avolve.model.failedfolder;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TestFailedFolderMapper {
	@Mock
	private DataFormatter df;
	@Mock
	private FormulaEvaluator eval;
	@Mock
	private Logger logger;

	@Test
	public void testScanRow() throws IOException {
		Row dataRow = mockDataRow(1, "./test", "197252", "1");

		FailedFolderMapper mapper = new FailedFolderMapper(df, eval, false);

		FailedFolder failedFolder = mapper.scanRow(dataRow);

		assertEquals(new File("test/197252").getCanonicalFile(),
				failedFolder.getFolderToSearch());
		assertEquals("197252", failedFolder.getFailedFolder());
	}

	@Test
	public void testScanRowProjectIdEnabled() throws IOException {
		Row dataRow = mockDataRow(1, "./test", "197252", "1");

		FailedFolderMapper mapper = new FailedFolderMapper(df, eval, true);

		FailedFolder failedFolder = mapper.scanRow(dataRow);

		assertEquals(new File("test/1/197252").getCanonicalFile(),
				failedFolder.getFolderToSearch());
		assertEquals("197252", failedFolder.getFailedFolder());
	}

	private Row mockDataRow(int rownum, String... cellContents) {
		Row dataRow = mock(Row.class);
		when(dataRow.getRowNum()).thenReturn(rownum);

		int colNum = 0;
		for(String cellValue : cellContents)
		{
			Cell cell = mock(Cell.class);
			when(dataRow.getCell(colNum++)).thenReturn(cell);
			when(df.formatCellValue(cell, eval)).thenReturn(cellValue);
		}
		
		return dataRow;
	}

}
