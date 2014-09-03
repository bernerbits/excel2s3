package net.bernerbits.client.avolve.model;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TestFailedFolderSpreadsheet {
	@Mock
	private Sheet sheet;
	@Mock
	private DataFormatter df;
	@Mock
	private FormulaEvaluator eval;
	@Mock
	private Logger logger;

	@Test
	public void testReadFailedFiles() throws IOException {
		Row topRow = mock(Row.class);
		when(sheet.getTopRow()).thenReturn((short) 0);
		when(topRow.getRowNum()).thenReturn(0);

		Row firstDataRow = mockDataRow(1, "./test", "197252", "1", "xxx.pdf");
		Row secondDataRow = mockDataRow(2, "./test", "21617", "2", "xxxA.pdf");

		when(sheet.iterator()).thenReturn(
				Arrays.asList(topRow, firstDataRow, secondDataRow).iterator());

		FailedFolderSpreadsheet spreadsheet = new FailedFolderSpreadsheet(
				sheet, df, eval);
		spreadsheet.setLogger(logger);
		
		List<FailedFolder> failedFolders = spreadsheet.readFailedFolders();
		assertEquals(2, failedFolders.size());
		
		FailedFolder f1 = failedFolders.get(0);
		assertEquals(new File("test/197252").getCanonicalFile(), f1.getFolderToSearch());
		assertEquals("197252", f1.getFailedFolder());
		assertEquals("1", f1.getProjectId());
		assertEquals("xxx.pdf", f1.getFileNamePattern());
		
		FailedFolder f2 = failedFolders.get(1);
		assertEquals(new File("test/21617").getCanonicalFile(), f2.getFolderToSearch());
		assertEquals("21617", f2.getFailedFolder());
		assertEquals("2", f2.getProjectId());
		assertEquals("xxxA.pdf", f2.getFileNamePattern());
	}
	
	private Row mockDataRow(int rownum, String sourceFolder,
			String failedFolder, String projectId, String filePattern) {
		Row dataRow = mock(Row.class);
		when(dataRow.getRowNum()).thenReturn(rownum);

		Cell sourceCell = mock(Cell.class);
		Cell folderCell = mock(Cell.class);
		Cell projectCell = mock(Cell.class);
		Cell patternCell = mock(Cell.class);

		when(dataRow.getCell(0)).thenReturn(sourceCell);
		when(dataRow.getCell(1)).thenReturn(folderCell);
		when(dataRow.getCell(2)).thenReturn(projectCell);
		when(dataRow.getCell(3)).thenReturn(patternCell);

		when(df.formatCellValue(sourceCell, eval)).thenReturn(sourceFolder);
		when(df.formatCellValue(folderCell, eval)).thenReturn(failedFolder);
		when(df.formatCellValue(projectCell, eval)).thenReturn(projectId);
		when(df.formatCellValue(patternCell, eval)).thenReturn(filePattern);

		return dataRow;
	}
}
