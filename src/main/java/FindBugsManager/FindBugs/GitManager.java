package FindBugsManager.FindBugs;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;

public class GitManager extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected File _file = null;
	protected String _path = null;

	public GitManager(File file, String path) {
		this._file = file;
		this._path = path;
	}

	public File getFile() {
		return _file;
	}

	public String getFilePath() {
		return _path;
	}

	public ArrayList<String> getAuthors(int startLine, int endLine) {
		return null;
	}

	public void display() {
		System.out.println("GitManager.");
	}

}
