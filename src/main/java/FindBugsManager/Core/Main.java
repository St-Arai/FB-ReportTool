package FindBugsManager.Core;

import java.io.File;

import javax.swing.JFrame;

import FindBugsManager.UI.LoginPage;

public class Main {

	// String bugOutputPath = Settings.getOutputDirectory();
	// private static final File gitFile = new File("C:/Projects/workspace/FBsample/.git");
	private static final File gitFile = new File("D:/Users/ALEXANDRITE/Projects/FBsample/.git");

	private static final String filePath = "src/src/FBsample.java";

	private Main() {

	}

	public static void main(String[] args) {
		new LoginPage(new JFrame());
	}

	public static File getGitFile() {
		return gitFile;
	}

	public static String getFilePath() {
		return filePath;
	}
}
