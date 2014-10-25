package net.bernerbits.client.avolve.model;

import java.io.File;
import java.io.IOException;

import org.apache.commons.codec.binary.Hex;

import com.amazonaws.util.Md5Utils;

public class LocalFile {

	private final File localFolder;
	private final File localFile;

	public LocalFile(File localFolder, File localFile) {
		this.localFolder = localFolder;
		this.localFile = localFile;
	}

	public String getLocalFolderName() {
		return localFolder.getName();
	}

	public File getLocalFolder() {
		return localFolder;
	}

	public File getLocalFile() {
		return localFile;
	}

	public String getUpstreamKey() {
		return localFolder.getName() + getRelativePath();
	}

	public String getRelativePath() {
		return localFile.getPath().replace(localFolder.getPath(), "")
				.replace('\\', '/');
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
