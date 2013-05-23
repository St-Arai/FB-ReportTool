package FindBugsManager.FindBugs;

import edu.umd.cs.findbugs.BugInstance;

public class BugInfo {

	private BugInstance _instance = null;

	private int _startLine = 0;
	private int _endLine = 0;

	private int _editedStartLine = 0;
	private int _editedEndLine = 0;

	private boolean _bugsExistFlag = false;

	private String _author = null;
	private EditType _type = null;

	public BugInfo(BugInstance instance, int start, int end) {
		_instance = instance;
		_startLine = start;
		_endLine = end;
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

	public int getEditedStartLine() {
		return _editedStartLine;
	}

	public int getEditedEndLine() {
		return _editedEndLine;
	}

	public String getAuthor() {
		return _author;
	}

	public EditType getEditType() {
		return _type;
	}

	public void setBugInstance(BugInstance instance) {
		_instance = instance;
	}

	public void setAuthor(String author) {
		_author = author;
	}

	public void setEditType(EditType type) {
		_type = type;
	}

	public void setEditedStartLine(int line) {
		_editedStartLine = line;
	}

	public void setEditedEndLine(int line) {
		_editedEndLine = line;
	}

	public boolean getExistFlag() {
		return _bugsExistFlag;
	}

	public void setExistFlag(boolean flag) {
		_bugsExistFlag = flag;
	}
}
