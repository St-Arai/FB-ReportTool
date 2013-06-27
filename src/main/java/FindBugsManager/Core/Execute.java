package FindBugsManager.Core;

import FindBugsManager.FindBugs.FindBugsManager;
import FindBugsManager.Git.PullManager;

public class Execute {
	private FindBugsManager manager = FindBugsManager.getInstance();
	private PullManager pull = new PullManager();

	private static final Execute execute = new Execute();
	private Execute() {

	}

	public void checkFixerName() {
		manager.compareBugInfoLists();
	}

	protected void startPullThread() {
		Thread pullThread = new Thread(pull);
		pullThread.start();
	}

	public void endPullThread() {
		pull.terminateAutoPull();
	}

	public void setPullStop() {
		pull.pullStop();
	}
	public void setPullResume() {
		pull.pullResume();
	}

	public static Execute getInctance() {
		return execute;
	}
}
