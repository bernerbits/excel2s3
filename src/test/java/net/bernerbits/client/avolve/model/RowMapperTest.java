package net.bernerbits.client.avolve.model;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public abstract class RowMapperTest {
	
	@Mock
	protected DataFormatter df;
	@Mock
	protected FormulaEvaluator eval;
	
	protected Row mockDataRow(int rownum, String... cellContents) {
		Row dataRow = mock(Row.class);
		when(dataRow.getRowNum()).thenReturn(rownum);

		int colNum = 0;
		for (String cellValue : cellContents) {
			Cell cell = mock(Cell.class);
			when(dataRow.getCell(colNum++)).thenReturn(cell);
			when(df.formatCellValue(cell, eval)).thenReturn(cellValue);
		}

		return dataRow;
	}
}
