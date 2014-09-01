package net.bernerbits.client.avolve.app;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import net.bernerbits.client.avolve.model.Bucket;
import net.bernerbits.client.avolve.model.Buckets;
import net.bernerbits.client.avolve.model.FailedFile;
import net.bernerbits.client.avolve.model.FailedFolderSpreadsheet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TestAppContext {

	@Mock
	private Buckets buckets;
	@Mock
	private FailedFolderSpreadsheet spreadsheet;

	@Test
	public void testReplaceFailedFiles() {
		FailedFile failedFile1 = mock(FailedFile.class);
		FailedFile failedFile2 = mock(FailedFile.class);
		FailedFile failedFile3 = mock(FailedFile.class);

		when(failedFile1.getProjectId()).thenReturn("1");
		when(failedFile2.getProjectId()).thenReturn("2");
		when(failedFile3.getProjectId()).thenReturn("1");

		Bucket bucket1 = mock(Bucket.class);
		Bucket bucket2 = mock(Bucket.class);

		when(buckets.getBucket("1")).thenReturn(bucket1);
		when(buckets.getBucket("2")).thenReturn(bucket2);

		when(spreadsheet.readFailedFiles()).thenReturn(
				Arrays.asList(failedFile1, failedFile2, failedFile3));

		AppContext appContext = new AppContext(spreadsheet, buckets);
		appContext.replaceFailedFiles();

		verify(bucket1).upload(failedFile1);
		verify(bucket1).verifyUpstream(failedFile1);
		verify(bucket2).upload(failedFile2);
		verify(bucket2).verifyUpstream(failedFile2);
		verify(bucket1).upload(failedFile3);
		verify(bucket1).verifyUpstream(failedFile3);
	}
}
