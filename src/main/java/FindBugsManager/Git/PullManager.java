package FindBugsManager.Git;

import java.io.File;
import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;

public class PullManager implements Runnable {

	private boolean _autoPullFlg = true;
	private boolean _threadWait = false;

	public PullManager() {

	}

	public void run() {
		CommandLine command = new CommandLine("cmd.exe");
		command.addArguments(new String[]{"/C", "start", "git", "pull", "origin", "master"});

		DefaultExecutor exe = new DefaultExecutor();
		exe.setWorkingDirectory(new File("../Experiment1-1"));
		try {
			exe.setExitValue(0);
			while (_autoPullFlg) {
				Thread.sleep(2000);
				synchronized (this) {
					while (_threadWait) {
						wait();
					}
				}
				exe.execute(command);
				Thread.sleep(2000);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void terminateAutoPull() {
		_autoPullFlg = false;
	}

	public synchronized void pullStop() {
		_threadWait = true;
	}

	public synchronized void pullResume() {
		_threadWait = false;
		notifyAll();
	}

}
