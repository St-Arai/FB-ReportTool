package FindBugsManager.FindBugs;

import java.io.File;

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

	public void display() {
		System.out.println("Here is GitManager class.");
	}

	public File getFile() {
		return _file;
	}

	public String getFilePath() {
		return _path;
	}
}
