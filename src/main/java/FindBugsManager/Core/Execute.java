package FindBugsManager.Core;

import java.io.File;

import FindBugsManager.FindBugs.BlameManager;
import FindBugsManager.FindBugs.DiffManager;
import FindBugsManager.FindBugs.FindBugsManager;

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

		manager.checkEditedBugs(diff, blame);
		// diff.display();
	}
}
