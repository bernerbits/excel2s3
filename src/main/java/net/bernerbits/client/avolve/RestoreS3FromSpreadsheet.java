package net.bernerbits.client.avolve;

import net.bernerbits.client.avolve.app.failedfolder.FailedFolderAppContext;
import net.bernerbits.client.avolve.app.failedfolder.FailedFolderConfig;

public class RestoreS3FromSpreadsheet {

	public static void main(String[] args) {
		if (args.length != 1) {
			System.err
					.println("Usage: xltos3 <path-to-config-file>");
			return;
		}
		FailedFolderConfig config = FailedFolderConfig.loadConfig(args[0]);
		FailedFolderAppContext context = config.getAppContext();
		context.replaceFailedFiles();
	}

}
