package net.bernerbits.client.avolve.model;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.s3.AmazonS3Client;

public class Buckets {

	private final Map<String, String> projectIdToBucketNameMap;
	private final Map<String, Bucket> bucketNameToBucketMap;
	private final AmazonS3Client s3Client;

	public Buckets(AmazonS3Client s3client,
			Map<String, String> projectIdToBucketNameMap) {
		this.s3Client = s3client;
		this.projectIdToBucketNameMap = projectIdToBucketNameMap;
		this.bucketNameToBucketMap = new HashMap<>();
	}

	public Bucket getBucket(String projectId) {
		String bucketName = projectIdToBucketNameMap.get(projectId);
		Bucket bucket = bucketNameToBucketMap.get(bucketName);
		if (bucket == null) {
			bucket = new Bucket(s3Client, bucketName);
			bucketNameToBucketMap.put(bucketName, bucket);
		}
		return bucket;
	}

}
