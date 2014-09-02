package net.bernerbits.client.avolve.model;

import java.io.File;
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

	private static final String SEP = File.separator;

	@Test
	public void testReadFailedFiles() {
		Row topRow = mock(Row.class);
		when(sheet.getTopRow()).thenReturn((short) 0);
		when(topRow.getRowNum()).thenReturn(0);

		Row firstDataRow = mockDataRow(1, "./test", "197252", "1", "xxx.pdf");

		when(sheet.iterator()).thenReturn(
				Arrays.asList(topRow, firstDataRow).iterator());

		FailedFolderSpreadsheet spreadsheet = new FailedFolderSpreadsheet(
				sheet, df, eval);
		spreadsheet.setLogger(logger);

		List<FailedFile> failedFiles = spreadsheet.readFailedFiles();

		assertEquals(2, failedFiles.size());

		FailedFile f1 = failedFiles.get(0);
		assertEquals("197252", f1.getFailedFolder());
		assertEquals("1", f1.getProjectId());
		assertEquals("197252/test_5dbef2a.pdf/test_5dbef2a.pdf",
				f1.getUpstreamKey());
		assertTrue(f1.getFileToReplace().getPath()
				.endsWith("197252" + SEP + "test_5dbef2a.pdf" + SEP + "test_5dbef2a.pdf"));

		FailedFile f2 = failedFiles.get(1);
		assertEquals("197252", f2.getFailedFolder());
		assertEquals("1", f2.getProjectId());
		assertEquals("197252/test_9b70a6c.pdf/test_9b70a6c.pdf",
				f2.getUpstreamKey());
		assertTrue(f2.getFileToReplace().getPath()
				.endsWith("197252" + SEP + "test_9b70a6c.pdf" + SEP + "test_9b70a6c.pdf"));

		verify(logger, never()).warn(any());
	}

	@Test
	public void testReadFailedFilesBadPattern() {
		Row topRow = mock(Row.class);
		when(sheet.getTopRow()).thenReturn((short) 0);
		when(topRow.getRowNum()).thenReturn(0);

		Row firstDataRow = mockDataRow(1, "./test", "197252", "1", "xx.pdf");

		when(sheet.iterator()).thenReturn(
				Arrays.asList(topRow, firstDataRow).iterator());

		FailedFolderSpreadsheet spreadsheet = new FailedFolderSpreadsheet(
				sheet, df, eval);
		spreadsheet.setLogger(logger);

		List<FailedFile> failedFiles = spreadsheet.readFailedFiles();

		assertEquals(0, failedFiles.size());
		verify(logger).warn(any());
	}

	@Test
	public void testReadFailedFilesSkipNonFolderFiles() {
		Row topRow = mock(Row.class);
		when(sheet.getTopRow()).thenReturn((short) 0);
		when(topRow.getRowNum()).thenReturn(0);

		Row firstDataRow = mockDataRow(1, "./test", "197252_bad", "1", "xxx.pdf");

		when(sheet.iterator()).thenReturn(
				Arrays.asList(topRow, firstDataRow).iterator());

		FailedFolderSpreadsheet spreadsheet = new FailedFolderSpreadsheet(
				sheet, df, eval);
		spreadsheet.setLogger(logger);

		List<FailedFile> failedFiles = spreadsheet.readFailedFiles();

		assertEquals(0, failedFiles.size());
		verify(logger, never()).warn(any());
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
