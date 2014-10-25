package net.bernerbits.client.avolve.model;

import java.io.File;
import java.io.IOException;

import org.apache.commons.codec.binary.Hex;

import com.amazonaws.util.Md5Utils;

public class LocalFile {
	
	private final File localFile;
	private final String upstreamKey;
	
	public LocalFile(File localFile, String upstreamKey) {
		this.localFile = localFile;
		this.upstreamKey = upstreamKey;
	}

	public File getLocalFile() {
		return localFile;
	}

	public String getUpstreamKey() {
		return upstreamKey;
	}

	public String getLocalMD5() {
		try {
			return new String(Hex.encodeHex(Md5Utils
					.computeMD5Hash(localFile)));
		} catch (IOException e) {
			System.err
					.println("WARNING! Unable to compute MD5 hash for local file "
							+ localFile.getPath() + ": " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

}
