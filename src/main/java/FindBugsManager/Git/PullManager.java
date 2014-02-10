package FindBugsManager.Git;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.errors.CanceledException;
import org.eclipse.jgit.api.errors.DetachedHeadException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidConfigurationException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepository;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import FindBugsManager.Core.Main;

public class PullManager implements Runnable {

	private boolean _autoPullFlg = true;
	private boolean _threadWait = false;

	private File _file = Main.getGitFile();
	private transient Repository repos = null;

	public PullManager() {

	}

	public void run() {
		// CommandLine command = new CommandLine("cmd.exe");
		// command.addArguments(new String[]{"/C", "git", "pull", "origin",
		// "master"});

		// command.addArgument("/C");
		// command.addArgument("git");
		// command.addArgument("pull");
		// command.addArgument("https://github.com/St-Arai/ExperimentTest.git");

		UsernamePasswordCredentialsProvider credential = new UsernamePasswordCredentialsProvider(
				"Satoshi Arai(Lab)", "satoshishy");

		try {
			repos = new FileRepository(_file);
			Git git = new Git(repos);
			PullCommand pull = git.pull();
			pull.setCredentialsProvider(credential);
			System.out.println("Credential");
			pull.call();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WrongRepositoryStateException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		} catch (DetachedHeadException e) {
			e.printStackTrace();
		} catch (InvalidRemoteException e) {
			e.printStackTrace();
		} catch (CanceledException e) {
			e.printStackTrace();
		} catch (RefNotFoundException e) {
			e.printStackTrace();
		} catch (NoHeadException e) {
			e.printStackTrace();
		} catch (TransportException e) {
			e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}

		// command.addArgument("master");
		// CommandLine command = new CommandLine("ping");
		// command.addArgument("/n");
		// command.addArgument("5");
		// command.addArguments("/w 1000");
		// command.addArgument("127.0.0.1");

		// DefaultExecutor exe = new DefaultExecutor();
		// exe.setWorkingDirectory(new File("../ExperimentTest"));
		// try {
		// exe.setExitValue(0);
		// while (_autoPullFlg) {
		// Thread.sleep(100);
		// System.out.println("Pulling...");
		// synchronized (this) {
		// while (_threadWait) {
		// wait();
		// }
		// }
		// exe.execute(command);
		// Thread.sleep(100);
		// }
		// } catch (IOException e) {
		// e.printStackTrace();
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
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
