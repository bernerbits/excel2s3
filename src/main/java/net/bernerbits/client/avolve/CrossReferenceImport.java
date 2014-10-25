package net.bernerbits.client.avolve;

import net.bernerbits.client.avolve.app.xref.CrossReferenceImportAppContext;
import net.bernerbits.client.avolve.app.xref.CrossReferenceImportConfig;

public class CrossReferenceImport {

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.err.println("Usage: xrefimport <path-to-config-file>");
			return;
		}
		CrossReferenceImportConfig config = CrossReferenceImportConfig
				.loadConfig(args[0]);
		CrossReferenceImportAppContext context = config.getAppContext();
		context.importNewFiles();
	}

}
