package FindBugsManager.Core;

import java.io.File;

import FindBugsManager.FindBugs.BlameManager;
import FindBugsManager.FindBugs.DiffManager;
import FindBugsManager.FindBugs.FindBugsManager;
import FindBugsManager.FindBugs.GitManager;
import FindBugsManager.UI.LoginPage;

public class Execute {
	private GitManager blame = null;
	private GitManager diff = null;

	private File gitFile = null;
	private String filePath = null;

	private FindBugsManager manager = null;

	public Execute(File file, String path) {
		this.gitFile = file;
		this.filePath = path;
		this.manager = FindBugsManager.getInstance();
	}

	public void run() {
		diff = new DiffManager(gitFile, filePath);
		blame = new BlameManager(gitFile, filePath);
		manager.checkEditedBugs(diff, blame);

		new LoginPage();
	}

	public void check() {
		diff = new DiffManager(gitFile, filePath);
		blame = new BlameManager(gitFile, filePath);
		manager.checkEditedBugs(diff, blame);
	}
}
