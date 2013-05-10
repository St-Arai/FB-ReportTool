package FindBugsManager.Core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import FindBugsManager.FindBugs.CommitInfo;
import FindBugsManager.FindBugs.CommitManager;
import FindBugsManager.FindBugs.FindBugsManager;

public class Main {
	public static void main(String[] args) {
		String bugOutputPath = Settings.getOutputDirectory();

		File output = new File(bugOutputPath + "output.xml");

		File gitFile = new File("C:/Projects/workspace/TeamGamification/.git");

		String filePath = "FBsample/src/src/FBsample.java";
		CommitManager commitMng = new CommitManager(gitFile, filePath);
		ArrayList<CommitInfo> info = commitMng.getCommitLog();

		String current = info.get(0).getCommitTime().toString() + " "
				+ info.get(0).getCommitMessage().replaceAll("\n", "");

		String previous = info.get(1).getCommitTime().toString() + " "
				+ info.get(1).getCommitMessage().replaceAll("\n", "");

		File bugOutput = new File(bugOutputPath + current + ".xml");
		File preOutput = new File(bugOutputPath + previous + ".xml");

		if (!(bugOutput.exists())) {
			File newfile = new File(bugOutputPath + current + ".xml");

			try {
				newfile.createNewFile();
			} catch (IOException e) {
				System.out.println(e);
			}
		}

		if (!(preOutput.exists())) {
			File newfile = new File(bugOutputPath + previous + ".xml");

			try {
				newfile.createNewFile();
			} catch (IOException e) {
				System.out.println(e);
			}
		}

		FindBugsManager manager = FindBugsManager.getInstance();
		XMLManager xml = new XMLManager();
		manager.createBugInfoList(bugOutput);
		manager.createPreBugInfoList(preOutput);

		Execute execute = new Execute(gitFile, filePath);
		execute.run();

		xml.createXML(manager);
	}
}
