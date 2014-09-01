package net.bernerbits.client.avolve.model;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.amazonaws.services.s3.AmazonS3Client;

@RunWith(MockitoJUnitRunner.class)
public class TestBuckets {

	@Mock
	private AmazonS3Client s3Client;
	
	private Map<String, String> projectIdToBucketNameMap;
	
	@Before
	public void setUp()
	{
		projectIdToBucketNameMap = new HashMap<String, String>();
		projectIdToBucketNameMap.put("1", "bucket1");
		projectIdToBucketNameMap.put("2", "bucketB");
	}
	
	@Test
	public void testGetBucket()
	{
		Buckets buckets = new Buckets(s3Client, projectIdToBucketNameMap);
		
		Bucket bucket1 = buckets.getBucket("1");
		assertEquals("bucket1", bucket1.getBucketName());
		assertSame(s3Client, bucket1.getS3client());

		Bucket otherBucket1 = buckets.getBucket("1");
		assertSame(bucket1, otherBucket1);

		Bucket bucket2 = buckets.getBucket("2");
		assertEquals("bucketB", bucket2.getBucketName());
		assertSame(s3Client, bucket2.getS3client());
	}
}
