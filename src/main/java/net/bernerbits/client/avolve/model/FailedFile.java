package net.bernerbits.client.avolve.model;

import java.io.File;
import java.io.IOException;

import org.apache.commons.codec.binary.Hex;

import com.amazonaws.util.Md5Utils;

public class FailedFile {

	private final File failedFolder;
	private final File fileToReplace;

	public FailedFile(File failedFolder, File fileToReplace) {
		this.failedFolder = failedFolder;
		this.fileToReplace = fileToReplace;
	}

	public String getFailedFolderName() {
		return failedFolder.getName();
	}

	public File getFailedFolder() {
		return failedFolder;
	}

	public File getFileToReplace() {
		return fileToReplace;
	}

	public String getUpstreamKey() {
		return failedFolder.getName()
				+ fileToReplace.getPath().replace(failedFolder.getPath(), "")
						.replace('\\', '/');
	}

	public String getLocalMD5() {
		try {
			return new String(Hex.encodeHex(Md5Utils
					.computeMD5Hash(fileToReplace)));
		} catch (IOException e) {
			System.err
					.println("WARNING! Unable to compute MD5 hash for local file "
							+ fileToReplace.getPath() + ": " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

}
