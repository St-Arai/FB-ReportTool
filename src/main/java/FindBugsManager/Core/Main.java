package FindBugsManager.Core;

import java.io.File;

import javax.swing.JFrame;

import FindBugsManager.UI.Login;

public class Main {

	// private static final File gitFile = new File("../Experiment/.git");
	private static final File gitFile = new File("../Experiment1-1/.git");
	// private static final File gitFile = new File("../Experiment1-2/.git");
	// private static final File gitFile = new File("../Experiment2-1/.git");
	// private static final File gitFile = new File("../Experiment2-2/.git");
	// private static final File gitFile = new File("../ExperimentTest/.git");
	// private static final File gitFile = new File("D:/Users/ALEXANDRITE/Projects/FBsample/.git");

	// private static final String targetPath =
	// "D:/Users/ALEXANDRITE/Projects/FBsample/bin/src/FBsample.class";
	private static final String targetPath = "D:/Users/ALEXANDRITE/Projects/Experiment1-1.fbp";
	// private static final String targetPath = "D:/Users/ALEXANDRITE/Projects/Experiment1-2.fbp";
	// private static final String targetPath = "D:/Users/ALEXANDRITE/Projects/Experiment2-1.fbp";
	// private static final String targetPath = "D:/Users/ALEXANDRITE/Projects/Experiment2-2.fbp";
	// private static final String targetPath = "D:/Users/ALEXANDRITE/Projects/ExperimentTest.fbp";
	// private static final String targetPath = "D:/Users/ALEXANDRITE/Projects/FBsample.fbp";
	// private static final String targetPath = "";
	// private static final String targetPath = "../FBsample/bin/src/FBsample.class";

	private static final String antXMLPath = "build1-1.xml";
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
