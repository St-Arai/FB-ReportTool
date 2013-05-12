package FindBugsManager.Core;

import java.io.File;

public class Main {
	public static void main(String[] args) {
		String bugOutputPath = Settings.getOutputDirectory2();

		File output = new File(bugOutputPath + "output.xml");

		// File gitFile = new File("C:/Projects/workspace/TeamGamification/.git");
		File gitFile = new File("D:/Users/ALEXANDRITE/Projects/FBsample/.git");

		String filePath = "src/src/FBsample.java";
		// CommitManager commitMng = new CommitManager(gitFile, filePath);

		// ArrayList<CommitInfo> info = commitMng.getCommitLog();
		// String current = info.get(0).getCommitMessage().replaceAll("\n", "");
		//
		// String previous = info.get(1).getCommitMessage().replaceAll("\n", "");

		Execute execute = new Execute(gitFile, filePath);
		execute.run();

	}
}
