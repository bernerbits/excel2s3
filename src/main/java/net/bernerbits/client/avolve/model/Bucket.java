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

	public void upload(LocalFile localFile) {
		synchronized(System.out)
		{
			System.out.println("Uploading file: "
					+ localFile.getLocalFile().getPath());
			System.out.println("\tBucket: " + bucketName);
			System.out.println("\tRemote file: " + localFile.getUpstreamKey());
		}
		s3client.putObject(bucketName, localFile.getUpstreamKey(),
				localFile.getLocalFile());
	}

	public void verifyUpstream(LocalFile localFile) {
		String upstreamMD5 = s3client.getObjectMetadata(bucketName,
				localFile.getUpstreamKey()).getETag();
		String localMD5 = localFile.getLocalMD5();
		if (localMD5 != null) {
			synchronized(System.out)
			{
				System.out.println("Verifying file: "
						+ localFile.getLocalFile().getPath());
				System.out.println("\tLocal MD5:  " + localMD5);
				System.out.println("\tRemote MD5: " + upstreamMD5);
				if (upstreamMD5.equals(localMD5)) {
					System.out.println("Files are identical!");
				} else {
					errlog.warn("WARNING! MD5 sum mismatch found for file: "
							+ localFile.getLocalFile().getPath()
							+ "\nFiles are not identical!");
				}
			}
		}
	}

	/*package*/ void setLogger(Logger logger) {
		this.errlog = logger;
	}

}
