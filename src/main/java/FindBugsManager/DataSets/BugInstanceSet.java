package FindBugsManager.DataSets;

import FindBugsManager.Git.EditType;
import edu.umd.cs.findbugs.BugInstance;

public class BugInstanceSet {

	private BugInstance _instance = null;

	private int _startLine = 0;
	private int _endLine = 0;

	private int _editedStartLine = 0;
	private int _editedEndLine = 0;

	private boolean _bugsExistFlag = false;

	private String _author = "None";
	private String _amender = null;

	private EditType _type = null;

	public BugInstanceSet(BugInstance instance, int start, int end) {
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

	public String getAmender() {
		return _amender;
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

	public void setAmender(String amender) {
		_amender = amender;
	}

	public void setEditType(EditType type) {
		_type = type;
	}

	public void setEditedStartLine(int startLine) {
		_editedStartLine = startLine;
	}

	public void setEditedEndLine(int endLine) {
		_editedEndLine = endLine;
	}

	public boolean getExistFlag() {
		return _bugsExistFlag;
	}

	public void setExistFlag(boolean flag) {
		_bugsExistFlag = flag;
	}
}
