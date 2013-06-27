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

	public void startPullThread() {
		pull.startAutoPull();
	}

	public void stopPullThread() {
		pull.terminateAutoPull();
	}

	public void pauseAutoPull() {
		pull.threadPause();
	}

	public void resumeAutoPull() {
		pull.threadResume();
	}

	public static Execute getInctance() {
		return execute;
	}
}
