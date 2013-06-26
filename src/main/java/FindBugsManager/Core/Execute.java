package FindBugsManager.Core;

import FindBugsManager.FindBugs.FindBugsManager;

public class Execute {
	private FindBugsManager manager = FindBugsManager.getInstance();

	public void checkFixerName() {
		manager.compareBugInfoLists();

	}
}
