package FindBugsManager.Core;

import java.io.File;

import FindBugsManager.FindBugs.FindBugsManager;
import FindBugsManager.Git.BlameManager;
import FindBugsManager.Git.DiffManager;

public class Execute {
	private FindBugsManager manager = FindBugsManager.getInstance();

	private BlameManager blame = null;
	private DiffManager diff = null;

	private File _gitFile = null;
	private String _filePath = null;

	public Execute(File file, String path) {
		_gitFile = file;
		_filePath = path;
	}

	public void checkFixerName() {
		diff = new DiffManager(_gitFile, _filePath);
		blame = new BlameManager(_gitFile, _filePath);

		diff.diffDriver();
		blame.blameDriver();

		// manager.checkEditedBugs(diff, blame);
		manager.compareBugInfoLists();
		manager.display();

	}
}
