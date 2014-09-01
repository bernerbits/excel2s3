package net.bernerbits.client.avolve;

import net.bernerbits.client.avolve.app.AppContext;
import net.bernerbits.client.avolve.app.Config;

public class RestoreS3FromSpreadsheet {

	public static void main(String[] args) {
		if (args.length != 1)
		{
			System.err.println("Usage: java -jar target/xltos3.jar <path-to-config-file>");
			return;
		}
		Config config = Config.loadConfig(args[0]);
		AppContext context = config.getAppContext();
		context.replaceFailedFiles();
	}

}
