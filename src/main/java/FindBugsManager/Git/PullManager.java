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
		command.addArguments(new String[]{"/C", "git", "pull", "origin",
				"master"});

		command.addArgument("/C");
		command.addArgument("git");
		command.addArgument("pull");
		command.addArgument("https://github.com/St-Arai/ExperimentTest.git");

		DefaultExecutor exe = new DefaultExecutor();
		exe.setWorkingDirectory(new File("../ExperimentTest"));
		try {
			exe.setExitValue(0);
			while (_autoPullFlg) {
				Thread.sleep(100);
				System.out.println("Pulling...");
				synchronized (this) {
					while (_threadWait) {
						wait();
					}
				}
				exe.execute(command);
				Thread.sleep(100);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public void startAutoPull() {
		_autoPullFlg = true;
		Thread pullThread = new Thread(this);
		pullThread.start();
	}

	public void terminateAutoPull() {
		_autoPullFlg = false;
	}

	public synchronized void threadPause() {
		_threadWait = true;
	}

	public synchronized void threadResume() {
		_threadWait = false;
		notifyAll();
	}

}
