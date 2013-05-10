package FindBugsManager.FindBugs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepository;
import org.gitective.core.BlobUtils;

public class DiffManager extends GitManager {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Collection<Edit> _edits = null;
	private ArrayList<BugInfo> infoList = null;

	private FindBugsManager manager = null;

	private final String branchName = "master";

	public DiffManager(File file, String path) {
		super(file, path);
		this.manager = FindBugsManager.getInstance();
		this.infoList = manager.getPreBugInfoList();
		diffDriver();
	}

	private void diffDriver() {
		Repository repo = null;
		try {
			repo = new FileRepository(_file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		ObjectId current = BlobUtils.getId(repo, branchName, _path);
		ObjectId previous = BlobUtils.getId(repo, branchName + "~1", _path);

		_edits = BlobUtils.diff(repo, previous, current);

		setPreviousInfo();
	}

	private void setPreviousInfo() {
		int bugLine = 0;
		int editStartLine = 0;
		int editEndLine = 0;

		if (_edits != null) {
			for (BugInfo info : infoList) {
				for (Edit edit : _edits) {
					bugLine = info.getStartLine();
					editStartLine = edit.getBeginA();
					editEndLine = edit.getEndA();
					if (editStartLine <= bugLine && bugLine <= editEndLine) {
						info.setEditType(changeEnumType(edit.getType()));
						info.setPreStartLine(edit.getBeginB());
						info.setPreEndLine(edit.getEndB());
						break;
					} else {
						info.setEditType(EditType.NO_CHANGE);
					}
				}
			}
		} else {
			System.out.println("No Edits...");
		}
	}

	private EditType changeEnumType(Edit.Type type) {
		EditType changedtype = null;
		switch (type) {
			case EMPTY :
				break;
			case INSERT :
				changedtype = EditType.NEW;
				break;
			case REPLACE :
				changedtype = EditType.EDIT;
				break;
			case DELETE :
				changedtype = EditType.DELETE;
				break;
			default :
				System.out.println("Unknown enum type...");
				break;
		}
		return changedtype;
	}

	public Collection<Edit> getEditList() {
		return _edits;
	}

	@Override
	public void display() {
		if (_edits != null) {
			for (Edit edit : _edits) {
				System.out.println(edit.getType());
				System.out.println(edit.getBeginA());
				System.out.println(edit.getEndA());
				System.out.println(edit.getBeginB());
				System.out.println(edit.getEndB());
			}
		} else {
			System.out.println("No Edits...");
		}
	}
}
