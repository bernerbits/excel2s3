package net.bernerbits.client.avolve.model.xref;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;

import net.bernerbits.client.avolve.model.sheet.IRowMapper;

public class ExistingFileMapper implements IRowMapper<String> {
	
	private final DataFormatter df;
	private final FormulaEvaluator eval;
	
	public ExistingFileMapper(DataFormatter df, FormulaEvaluator eval) {
		this.df = df;
		this.eval = eval;
	}

	@Override
	public String scanRow(Row row) {
		return df.formatCellValue(row.getCell(4), eval);
	}

}
