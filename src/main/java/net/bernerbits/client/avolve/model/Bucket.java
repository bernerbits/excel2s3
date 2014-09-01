package net.bernerbits.client.avolve.model;

import org.apache.log4j.Logger;

import com.amazonaws.services.s3.AmazonS3Client;

public class Bucket {
	private Logger errlog = Logger.getLogger("errlog");
	
	private final AmazonS3Client s3client;
	private final String bucketName;

	public Bucket(AmazonS3Client s3client, String bucketName) {
		this.s3client = s3client;
		this.bucketName = bucketName;
	}

	public AmazonS3Client getS3client() {
		return s3client;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void upload(FailedFile failedFile) {
		System.out.println("Uploading file: "
				+ failedFile.getFileToReplace().getPath());
		System.out.println("\tBucket: " + bucketName);
		System.out.println("\tRemote file: " + failedFile.getUpstreamKey());

		s3client.putObject(bucketName, failedFile.getUpstreamKey(),
				failedFile.getFileToReplace());
	}

	public void verifyUpstream(FailedFile failedFile) {
		System.out.println("Verifying file: "
				+ failedFile.getFileToReplace().getPath());
		String upstreamMD5 = s3client.getObjectMetadata(bucketName,
				failedFile.getUpstreamKey()).getETag();
		String localMD5 = failedFile.getLocalMD5();
		if (localMD5 != null) {
			System.out.println("\tLocal MD5:  " + localMD5);
			System.out.println("\tRemote MD5: " + upstreamMD5);
			if (upstreamMD5.equals(localMD5)) {
				System.out.println("Files are identical!");
			} else {
				errlog.warn("WARNING! MD5 sum mismatch found for file: "
						+ failedFile.getFileToReplace().getPath()
						+ "\nFiles are not identical!");
			}
		}
	}

	/*package*/ void setLogger(Logger logger) {
		this.errlog = logger;
	}

}
