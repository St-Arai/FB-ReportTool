package FindBugsManager.Core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import FindBugsManager.FindBugs.CommitInfo;
import FindBugsManager.FindBugs.CommitManager;
import FindBugsManager.FindBugs.FindBugsManager;

public class Main {
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		String bugOutputPath = Settings.getOutputDirectory();

		File output = new File(bugOutputPath + "output.xml");

		//File gitFile = new File("C:/Projects/workspace/TeamGamification/.git");
		File gitFile = new File("D:/Users/ALEXANDRITE/Projects/FBsample");

		String filePath = "FBsample/src/src/FBsample.java";
		CommitManager commitMng = new CommitManager(gitFile, filePath);
		ArrayList<CommitInfo> info = commitMng.getCommitLog();

		// String current = info.get(0).getCommitName();
		String current = info.get(0).getCommitTime().toString() + " "
				+ info.get(0).getCommitMessage().replaceAll("\n", "");
		// String previous = info.get(1).getCommitName();
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

		if (output.length() != bugOutput.length()) {
			try {
				FileChannel copyPreOutput = new FileInputStream(bugOutput)
						.getChannel();
				FileChannel preOutputDestination = new FileOutputStream(
						preOutput).getChannel();
				copyPreOutput.transferTo(0, copyPreOutput.size(),
						preOutputDestination);
				copyPreOutput.close();
				preOutputDestination.close();

				FileChannel copyOutput = new FileInputStream(output)
						.getChannel();
				FileChannel outputDestination = new FileOutputStream(bugOutput)
						.getChannel();

				copyOutput.transferTo(0, copyOutput.size(), outputDestination);
				copyOutput.close();
				outputDestination.close();

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		FindBugsManager manager = FindBugsManager.getInstance();
		XMLManager xml = new XMLManager();
		manager.createBugInfoList(bugOutput);
		manager.createPreBugInfoList(preOutput);

		// File gitFile = new File("C:/TeamGamification/.git");
		// String filePath = "FBsample/src/src/FBsample.java";

		Execute execute = new Execute(gitFile, filePath);
		execute.run();
		// manager.display();
		// manager.displayAll();
		xml.createXML(manager);

	}
}
