package FindBugsManager.FindBugs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jgit.api.BlameCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepository;

import edu.umd.cs.findbugs.BugInstance;

public class BlameManager extends GitManager {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5815629130134748289L;

	private BlameResult result = null;
	private ArrayList<BugInfo> infoList = null;

	private FindBugsManager manager = null;

	public BlameManager(File file, String path) {
		super(file, path);
		this.manager = FindBugsManager.getInstance();
		this.infoList = manager.getBugInfoList();
		blameDriver();
	}

	private void blameDriver() {
		Repository repos;
		try {
			repos = new FileRepository(_file);
			Git git = new Git(repos);

			BlameCommand blame = git.blame();
			blame.setFilePath(_path);
			result = blame.call();
			setAuthor();

		} catch (NoHeadException e) {
			e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<String> getAuthors(int startLine, int endLine) {
		ArrayList<String> authors = new ArrayList<String>();
		for (int i = startLine; i <= endLine; i++) {
			authors.add(result.getSourceAuthor(i).getName());
		}
		return authors;
	}

	private void setAuthor() {
		int bugLine = 0;

		try {
			for (BugInfo info : infoList) {
				bugLine = info.getStartLine();
				int checkLine = result.getSourceLine(bugLine);
				String author = result.getSourceAuthor(checkLine).getName();
				info.setAuthor(author);
			}
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void display() {
		BugInstance instance;
		try {
			for (BugInfo info : infoList) {
				instance = info.getBugInstance();
				manager.displayPatternInfo(instance);
				System.out.println("Author : " + info.getAuthor());
				System.out.println("Line : " + info.getStartLine());
				System.out.println();
			}
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}

}
