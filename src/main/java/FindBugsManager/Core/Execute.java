package FindBugsManager.Core;

import java.io.File;

import FindBugsManager.FindBugs.BlameManager;
import FindBugsManager.FindBugs.CommitManager;
import FindBugsManager.FindBugs.DiffManager;
import FindBugsManager.FindBugs.FindBugsManager;

public class Execute {
	private BlameManager blame = null;
	private DiffManager diff = null;
	private CommitManager commit = null;

	private File gitFile = null;
	private String filePath = null;

	public Execute(File file, String path) {
		this.gitFile = file;
		this.filePath = path;
	}

	public void run() {
		FindBugsManager manager = FindBugsManager.getInstance();

		commit = new CommitManager(gitFile, filePath);
		diff = new DiffManager(gitFile, filePath);
		blame = new BlameManager(gitFile, filePath);
		manager.checkEditedBugs(diff, blame);

		// blame.display();
		// diff.display();
		commit.display();
	}
}
