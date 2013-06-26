package FindBugsManager.Core;

import java.io.File;

import javax.swing.JFrame;

import FindBugsManager.Git.PullManager;
import FindBugsManager.UI.Login;

public class Main {

	private static final File gitFile = new File(Settings.getPath11());
	private static final String targetPath = Settings.getFbpPath11();

	private static final String antXMLPath = "build1-1.xml";
	private static final String filePath = "src/src/FBsample.java";

	private Main() {

	}

	public static void main(String[] args) {
		new Login(new JFrame());
		PullManager pull = new PullManager();
		Thread pullThread = new Thread(pull);
		pullThread.start();
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
