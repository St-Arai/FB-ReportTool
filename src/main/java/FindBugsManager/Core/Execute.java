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

	private FindBugsManager manager = null;

	public Execute(File file, String path) {
		this.gitFile = file;
		this.filePath = path;
		this.manager = FindBugsManager.getInstance();
	}

	public void run() {
		commit = new CommitManager(gitFile, filePath);
		diff = new DiffManager(gitFile, filePath);
		blame = new BlameManager(gitFile, filePath);
		manager.checkEditedBugs(diff, blame);

		commit.display();
	}

	public void check() {
		diff = new DiffManager(gitFile, filePath);
		blame = new BlameManager(gitFile, filePath);
		manager.checkEditedBugs(diff, blame);
	}
}
