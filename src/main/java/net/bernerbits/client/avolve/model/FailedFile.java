package net.bernerbits.client.avolve.model;

import java.io.File;
import java.io.IOException;

import org.apache.commons.codec.binary.Hex;

import com.amazonaws.util.Md5Utils;

public class FailedFile {

	private final String failedFolder;
	private final String projectId;
	private final File fileToReplace;

	public FailedFile(String failedFolder, String projectId, File fileToReplace) {
		this.failedFolder = failedFolder;
		this.projectId = projectId;
		this.fileToReplace = fileToReplace;
	}

	public String getFailedFolder() {
		return failedFolder;
	}

	public String getProjectId() {
		return projectId;
	}

	public File getFileToReplace() {
		return fileToReplace;
	}

	public String getUpstreamKey() {
		String simpleFileName = fileToReplace.getName();
		return failedFolder + "/" + simpleFileName + "/" + simpleFileName;
	}

	public String getLocalMD5() {
		try {
			return new String(Hex.encodeHex(Md5Utils.computeMD5Hash(fileToReplace)));
		} catch (IOException e) {
			System.err
					.println("WARNING! Unable to compute MD5 hash for local file "
							+ fileToReplace.getPath() + ": " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

}
