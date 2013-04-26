package FindBugsManager.FindBugs;

import edu.umd.cs.findbugs.BugInstance;

public class BugInfo {

	private BugInstance _instance = null;

	private int _startLine = 0;
	private int _endLine = 0;

	private int _preStartLine = 0;
	private int _preEndLine = 0;

	private boolean flag = false;

	private String _author = null;
	private EditType _type = null;

	public BugInfo(BugInstance instance, int start, int end) {
		this._instance = instance;
		this._startLine = start;
		this._endLine = end;
	}

	public BugInstance getBugInstance() {
		return _instance;
	}

	public int getStartLine() {
		return _startLine;
	}

	public int getEndLine() {
		return _endLine;
	}

	public int getPreStartLine() {
		return _preStartLine;
	}

	public int getPreEndLine() {
		return _preEndLine;
	}

	public String getAuthor() {
		return _author;
	}

	public EditType getEditType() {
		return _type;
	}

	public void setBugInstance(BugInstance instance) {
		this._instance = instance;
	}

	public void setAuthor(String author) {
		this._author = author;
	}

	public void setEditType(EditType type) {
		this._type = type;
	}

	public void setPreStartLine(int line) {
		this._preStartLine = line;
	}

	public void setPreEndLine(int line) {
		this._preEndLine = line;
	}

	public boolean getExistFlag() {
		return flag;
	}
	public void setExistFlag() {
		this.flag = true;
	}
}
