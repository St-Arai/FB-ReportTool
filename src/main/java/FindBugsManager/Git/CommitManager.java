package FindBugsManager.Git;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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

	private transient ArrayList<CommitInfo> commitLog = new ArrayList<CommitInfo>();

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
				String commit = revCommit.getName();
				String author = revCommit.getAuthorIdent().getName();
				int time = revCommit.getCommitTime();
				String message = revCommit.getFullMessage();
				commitLog.add(new CommitInfo(commit, author, time, message));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoHeadException e) {
			e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
	}

	public String[] getCommitList() {
		String[] info = new String[commitLog.size()];
		int i = 0;
		for (CommitInfo commit : commitLog) {
			info[i] = "<html>"
					+ commit.getCommitTime()
					+ "    :    "
					+ commit.getCommitter()
					+ "<br/>"
					+ commit.getCommitMessage().replaceAll("'", "").replaceAll("/", "")
							.replaceAll(".com", "") + "</html>";
			i++;
		}
		return info;
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
