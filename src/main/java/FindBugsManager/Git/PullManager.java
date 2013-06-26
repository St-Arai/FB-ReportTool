package FindBugsManager.Git;

import java.io.File;
import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;

public class PullManager implements Runnable {

	public PullManager() {

	}

	public void run() {

		CommandLine command = new CommandLine("cmd.exe");
		command.addArguments(new String[]{"/C", "git", "pull", "origin", "master"});

		DefaultExecutor exe = new DefaultExecutor();
		try {
			exe.setWorkingDirectory(new File("../Experiment1-1"));
			exe.setExitValue(0);
			while (true) {
				exe.execute(command);
				Thread.sleep(3000);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
