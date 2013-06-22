package FindBugsManager.Git;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JComboBox;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepository;

import FindBugsManager.Core.Settings;

public class CommitManager {

	private ArrayList<CommitInfo> commitLog = new ArrayList<CommitInfo>();
	private ArrayList<CommitInfo> parentLog = new ArrayList<CommitInfo>();

	private JComboBox<String> checkoutBranches = new JComboBox<String>();
	private JComboBox<String> targetBugFile = new JComboBox<String>();

	private String bugDataPath = Settings.getBugDataStorePath();
	private final File bugDataDirectory = new File(bugDataPath);

	private File _file = null;

	public CommitManager(File file) {
		_file = file;
	}

	public void setCommitLogs() {
		Repository repos = null;
		try {
			repos = new FileRepository(_file);
			Git git = new Git(repos);

			LogCommand log = git.log();
			for (RevCommit revCommit : log.call()) {

				String message = revCommit.getFullMessage();
				System.out.print(message);
				RevCommit[] parents = revCommit.getParents();
				System.out.println("======================");
				for (RevCommit com : parents) {
					System.out.println(com.getAuthorIdent().getName());
					System.out.print(com.getFullMessage());
					System.out.println("======================");
				}
				System.out.println("\n");
				commitLog.add(new CommitInfo(revCommit, parents));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoHeadException e) {
			e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
	}

	public String[] getAllCommitList() {
		int length = commitLog.size();
		String[] info = new String[length];
		int i = 0;
		for (CommitInfo commit : commitLog) {
			int number = length - i;
			commit.setCommitNumber(number);
			String message = String.valueOf(number) + ":" + commit.getCommitMessage();
			int lastIndex = message.lastIndexOf("Conflicts:");
			if (lastIndex > 0) {
				message = message.substring(0, lastIndex) + " Conflict.";
			}
			info[i] = "<html>" + commit.getCommitDate() + "    :    " + commit.getCommitter()
					+ "<br/>" + message + "</html>";
			i++;
		}
		return info;
	}

	public String getCommitList(CommitInfo parent) {
		int parentTime = parent.getCommitTime();
		String parentInfo = "null";
		String[] info = getAllCommitList();
		int count = info.length - 1;
		ArrayList<CommitInfo> reverse = new ArrayList<CommitInfo>(commitLog);
		Collections.reverse(reverse);
		parentLog = new ArrayList<CommitInfo>();
		for (CommitInfo commit : reverse) {
			int time = commit.getCommitTime();
			if (time == parentTime) {
				parentLog.add(commit);
				parentInfo = info[count];
				break;
			}
			count--;
		}
		return parentInfo;
	}

	public void initBugFileList() {
		File[] bugFiles = bugDataDirectory.listFiles();
		String[] info = new String[bugFiles.length];
		int i = 0;

		for (File file : bugFiles) {
			info[i] = file.getName().replace(".xml", "");
			i++;
		}
		targetBugFile = new JComboBox<String>(info);
	}

	public ArrayList<CommitInfo> getCommitLog() {
		return commitLog;
	}

	public ArrayList<CommitInfo> getParentLog() {
		return parentLog;
	}

	public int getCommitLength() {
		return commitLog.size();
	}

	public JComboBox<String> getBranchList() {
		return checkoutBranches;
	}

	public JComboBox<String> getTargetBugFileList() {
		return targetBugFile;
	}

	public void display() {
		System.out.println("CommitManager.");
	}
}
