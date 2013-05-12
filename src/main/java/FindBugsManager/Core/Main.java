package FindBugsManager.Core;

import java.io.File;

public class Main {
	public static void main(String[] args) {
		// String bugOutputPath = Settings.getOutputDirectory();

		// File gitFile = new File("C:/Projects/workspace/TeamGamification/.git");
		File gitFile = new File("D:/Users/ALEXANDRITE/Projects/FBsample/.git");

		String filePath = "src/src/FBsample.java";

		Execute execute = new Execute(gitFile, filePath);
		execute.run();

	}
}
