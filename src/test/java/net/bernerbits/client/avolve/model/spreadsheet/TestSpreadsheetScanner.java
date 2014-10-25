package net.bernerbits.client.avolve.model.spreadsheet;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import net.bernerbits.client.avolve.model.failedfolder.FailedFolder;
import net.bernerbits.client.avolve.model.sheet.IRowMapper;
import net.bernerbits.client.avolve.model.sheet.SpreadsheetScanner;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TestSpreadsheetScanner {
	@Mock
	private Sheet sheet;
	@Mock
	private IRowMapper<Object> rowMapper;

	@Test
	public void testScan() throws IOException {
		Row topRow = mock(Row.class);
		when(sheet.getTopRow()).thenReturn((short) 0);
		when(topRow.getRowNum()).thenReturn(0);

		Row firstDataRow = mock(Row.class);
		when(firstDataRow.getRowNum()).thenReturn(1);
		Row secondDataRow = mock(Row.class);
		when(secondDataRow.getRowNum()).thenReturn(2);

		Object firstDataObject = mock(Object.class);
		Object secondDataObject = mock(Object.class);

		when(rowMapper.scanRow(firstDataRow)).thenReturn(firstDataObject);
		when(rowMapper.scanRow(secondDataRow)).thenReturn(secondDataObject);

		when(sheet.iterator()).thenReturn(
				Arrays.asList(topRow, firstDataRow, secondDataRow).iterator());

		SpreadsheetScanner<Object> scanner = new SpreadsheetScanner<Object>(rowMapper);

		List<Object> scannedData = scanner.scan(sheet);
		
		assertEquals(2, scannedData.size());

		Object data1 = scannedData.get(0);
		assertEquals(data1, firstDataObject);

		Object data2 = scannedData.get(1);
		assertEquals(data2, secondDataObject);
	}

}
