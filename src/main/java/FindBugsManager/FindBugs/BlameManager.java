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

public class BlameManager {

	private FindBugsManager manager = FindBugsManager.getInstance();

	private BlameResult result = null;
	private transient ArrayList<BugInfo> infoList = manager.getBugInfoList();

	private File _file = null;
	private String _path = null;

	public BlameManager(File file, String path) {
		_file = file;
		_path = path;
	}

	public void blameDriver() {
		Repository repos;
		try {
			repos = new FileRepository(_file);
			Git git = new Git(repos);

			BlameCommand blame = git.blame();
			blame.setFilePath(_path);
			result = blame.call();
		} catch (NoHeadException e) {
			e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		setAuthor();
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

	public void display() {
		System.out.println("BlameManager.");
	}

}
