package net.bernerbits.client.avolve.model.sheet;

import org.apache.poi.ss.usermodel.Row;

public interface IRowMapper<T> {
	T scanRow(Row row);
}
