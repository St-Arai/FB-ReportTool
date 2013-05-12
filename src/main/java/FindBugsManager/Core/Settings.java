package FindBugsManager.Core;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Settings {

	private static Properties properties = new Properties();

	static {
		try {
			properties.load(new FileInputStream("settings.properties"));
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}

	private Settings() {
	}

	public static String getOutputDirectory() {
		return properties.getProperty("outputDirectory");
	}

	public static String getOutputDirectory2() {
		return properties.getProperty("outputDirectory2");
	}
}
