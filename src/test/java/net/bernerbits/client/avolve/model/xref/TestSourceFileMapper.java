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
public class TestSourceFileMapper extends RowMapperTest {
	@Test
	public void testScanRow() throws IOException {
		Row dataRow = mockDataRow(1, "123", null, null, null, null, null, null,
				null, null, null, "//prefix/file");

		SourceFileMapper mapper = new SourceFileMapper(df, eval, "//prefix",
				new File("/home"));

		LocalFile file = mapper.scanRow(dataRow);

		assertEquals("/home/file/file", file.getLocalFile().getPath());
		assertEquals("123/file/file", file.getUpstreamKey());
	}
	
	@Test
	public void testScanRowSpecificFile() throws IOException {
		Row dataRow = mockDataRow(1, "456", null, null, null, null, null, null,
				null, null, null, "//prefix/file/file_V2");

		SourceFileMapper mapper = new SourceFileMapper(df, eval, "//prefix",
				new File("/home"));

		LocalFile file = mapper.scanRow(dataRow);

		assertEquals("/home/file/file_V2", file.getLocalFile().getPath());
		assertEquals("456/file/file_V2", file.getUpstreamKey());
	}
}
