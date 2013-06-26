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

	public static String getPath11() {
		return properties.getProperty("experiment1-1");
	}
	public static String getPath12() {
		return properties.getProperty("experiment1-2");
	}
	public static String getPath21() {
		return properties.getProperty("experiment2-1");
	}
	public static String getPath22() {
		return properties.getProperty("experiment2-2");
	}
	public static String getPathTest() {
		return properties.getProperty("experimentTest");
	}

	public static String getFbpPath11() {
		return properties.getProperty("experiment1-1-fbp");
	}
	public static String getFbpPath12() {
		return properties.getProperty("experiment1-2-fbp");
	}
	public static String getFbpPath21() {
		return properties.getProperty("experiment2-1-fbp");
	}
	public static String getFbpPath22() {
		return properties.getProperty("experiment2-2-fbp");
	}
	public static String getFbpPathTest() {
		return properties.getProperty("experimentTest-fbp");
	}

}
