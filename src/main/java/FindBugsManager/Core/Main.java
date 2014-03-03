package FindBugsManager.Core;

import java.io.File;

import javax.swing.JFrame;

import FindBugsManager.UI.Login;

public class Main {

	private static final File gitFile = new File(Settings.getPathSample());
	private static final String targetPath = Settings.getFbpPathSample();

	private static final String antXMLPath = "fbsample.xml";

	private static final String filePath = "src/src/FBsample.java";

	private Main() {

	}

	public static void main(String[] args) {
		new Login(new JFrame());
	}

	public static File getGitFile() {
		return gitFile;
	}

	public static String getFilePath() {
		return filePath;
	}

	public static String getTargetPath() {
		return targetPath;
	}

	public static String getAntXML() {
		return antXMLPath;
	}

}
