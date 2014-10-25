package net.bernerbits.client.avolve.model;

import java.io.File;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

import org.mockito.runners.MockitoJUnitRunner;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;

@RunWith(MockitoJUnitRunner.class)
public class TestBucket {
	@Mock
	private Logger logger;
	
	@Mock
	private AmazonS3Client s3Client;
	
	@Test
	public void testUpload()
	{
		File actualFile = new File("folder/test.pdf/test_v1.pdf");
		LocalFile failedFile = new LocalFile(actualFile, "key");
		
		Bucket bucket = new Bucket(s3Client, "bucket");
		bucket.upload(failedFile);
		
		verify(s3Client).putObject("bucket", "key", actualFile);
	}
	
	@Test
	public void testVerifyUpstream()
	{
		File actualFile = new File("folder/test.pdf/test_v1.pdf");
		LocalFile failedFile = spy(new LocalFile(actualFile, "key"));
		
		ObjectMetadata objectMetadata = mock(ObjectMetadata.class);
		when(objectMetadata.getETag()).thenReturn("abcdef");
		
		doReturn("abcdef").when(failedFile).getLocalMD5();
		when(s3Client.getObjectMetadata("bucket", "key")).thenReturn(objectMetadata);
		
		Bucket bucket = new Bucket(s3Client, "bucket");
		bucket.setLogger(logger);
		bucket.verifyUpstream(failedFile);
		
		verify(logger, never()).warn(any());
	}
	
	@Test
	public void testVerifyUpstreamFailed()
	{
		File actualFile = new File("folder/test.pdf/test_v1.pdf");
		LocalFile failedFile = spy(new LocalFile(actualFile, "key"));
		
		ObjectMetadata objectMetadata = mock(ObjectMetadata.class);
		when(objectMetadata.getETag()).thenReturn("abcdef");
		
		doReturn("fedcba").when(failedFile).getLocalMD5();
		when(s3Client.getObjectMetadata("bucket", "key")).thenReturn(objectMetadata);
		
		Bucket bucket = new Bucket(s3Client, "bucket");
		bucket.setLogger(logger);
		bucket.verifyUpstream(failedFile);
		
		verify(logger).warn(any());
	}
	
	@Test
	public void testVerifyUpstreamNullLocalMD5()
	{
		File actualFile = new File("folder/test.pdf/test_v1.pdf");
		LocalFile failedFile = spy(new LocalFile(actualFile, "key"));
		
		ObjectMetadata objectMetadata = mock(ObjectMetadata.class);
		when(objectMetadata.getETag()).thenReturn("abcdef");
		
		doReturn(null).when(failedFile).getLocalMD5();
		when(s3Client.getObjectMetadata("bucket", "key")).thenReturn(objectMetadata);
		
		Bucket bucket = new Bucket(s3Client, "bucket");
		bucket.setLogger(logger);
		bucket.verifyUpstream(failedFile);
		
		verify(logger, never()).warn(any());
	}
}
