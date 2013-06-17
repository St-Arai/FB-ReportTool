package FindBugsManager.Core;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Settings {

	private static Properties properties = new Properties();

	static {
		FileInputStream fileInput = null;
		try {
			fileInput = new FileInputStream("settings.properties");
			properties.load(fileInput);
		} catch (IOException e2) {
			e2.printStackTrace();
		} finally {
			if (fileInput != null) {
				try {
					fileInput.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private Settings() {

	}

	public static String getOutputPath() {
		return properties.getProperty("outputpath");
	}

	public static String getBugDataStorePath() {
		return properties.getProperty("bugdatastorepath");
	}
}
