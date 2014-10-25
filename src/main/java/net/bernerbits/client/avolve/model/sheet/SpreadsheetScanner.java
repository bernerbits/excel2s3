package net.bernerbits.client.avolve.model.sheet;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class SpreadsheetScanner<T> {

	private final IRowMapper<T> rowMapper;

	public SpreadsheetScanner(IRowMapper<T> rowMapper) {
		this.rowMapper = rowMapper;
	}

	public List<T> scan(Sheet sheet) {
		List<T> data = new ArrayList<>();
		for (Row currentRow : sheet) {
			if (currentRow.getRowNum() == sheet.getTopRow()) {
				continue;
			}
			T element = rowMapper.scanRow(currentRow);
			if(element != null)
			{
				data.add(element);
			}
		}
		return data;
	}

}
