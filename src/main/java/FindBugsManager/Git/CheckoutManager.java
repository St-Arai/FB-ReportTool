package FindBugsManager.Git;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepository;

import FindBugsManager.Core.Main;

public class CheckoutManager {

	private transient Repository repos = null;

	private File _file = Main.getGitFile();

	public CheckoutManager() {

	}

	public void checkoutCommand(String selectedCommit) {
		try {
			repos = new FileRepository(_file);
			Git git = new Git(repos);
			CheckoutCommand check = git.checkout();
			check.setName(selectedCommit);
			check.call();

		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (RefAlreadyExistsException e1) {
			e1.printStackTrace();
		} catch (RefNotFoundException e1) {
			e1.printStackTrace();
		} catch (InvalidRefNameException e1) {
			e1.printStackTrace();
		} catch (CheckoutConflictException e1) {
			e1.printStackTrace();
		} catch (GitAPIException e1) {
			e1.printStackTrace();
		}
	}

	public void backtoLatestRevision() {
		try {
			repos = new FileRepository(_file);
			Git git = new Git(repos);
			CheckoutCommand check = git.checkout();
			check.setName("master");
			check.call();

		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (RefAlreadyExistsException e1) {
			e1.printStackTrace();
		} catch (RefNotFoundException e1) {
			e1.printStackTrace();
		} catch (InvalidRefNameException e1) {
			e1.printStackTrace();
		} catch (CheckoutConflictException e1) {
			e1.printStackTrace();
		} catch (GitAPIException e1) {
			e1.printStackTrace();
		}
	}

}
