package FindBugsManager.Git;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepository;
import org.gitective.core.BlobUtils;

import FindBugs.DataSets.BugInstanceSet;
import FindBugsManager.FindBugs.FindBugsManager;

public class DiffManager {

	private transient FindBugsManager manager = FindBugsManager.getInstance();

	private Collection<Edit> edits = null;

	private transient ArrayList<BugInstanceSet> infoList = manager.getBugInfoList();
	private transient ArrayList<BugInstanceSet> preInfoList = manager.getPreBugInfoList();;

	private final static String branchName = "master";

	private File _file = null;
	private String _path = null;

	public DiffManager(File file, String path) {
		_file = file;
		_path = path;
	}

	public void diffDriver() {
		Repository repo = null;
		try {
			repo = new FileRepository(_file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		ObjectId current = BlobUtils.getId(repo, branchName, _path);
		ObjectId previous = BlobUtils.getId(repo, branchName + "~1", _path);

		edits = BlobUtils.diff(repo, previous, current);

		setInfo(infoList);
		setPreviousInfo(preInfoList);
	}

	private void setPreviousInfo(ArrayList<BugInstanceSet> infoList) {
		int bugLine = 0;
		int editStartLine = 0;
		int editEndLine = 0;

		if (edits != null) {
			for (BugInstanceSet bugInfo : infoList) {
				for (Edit edit : edits) {
					bugLine = bugInfo.getStartLine();
					editStartLine = edit.getBeginA();
					editEndLine = edit.getEndA();
					if (editStartLine <= bugLine && bugLine <= editEndLine) {
						bugInfo.setEditType(changeEnumType(edit.getType()));
						bugInfo.setEditedStartLine(edit.getBeginB());
						bugInfo.setEditedEndLine(edit.getEndB());
						break;
					} else {
						bugInfo.setEditType(EditType.NO_CHANGE);
					}
				}
				System.out.println(bugInfo.getBugInstance().getBugPattern().getType());
				System.out.println(bugInfo.getEditType() + "\n");
			}
		} else {
			System.out.println("No Edits...");
		}
	}
	private void setInfo(ArrayList<BugInstanceSet> infoList) {
		int bugLine = 0;
		int editStartLine = 0;
		int editEndLine = 0;

		if (edits != null) {
			for (BugInstanceSet bugInfo : infoList) {
				for (Edit edit : edits) {
					bugLine = bugInfo.getStartLine();
					editStartLine = edit.getBeginB();
					editEndLine = edit.getEndB();
					if (editStartLine <= bugLine && bugLine <= editEndLine) {
						bugInfo.setEditType(changeEnumType(edit.getType()));
						bugInfo.setEditedStartLine(edit.getBeginA());
						bugInfo.setEditedEndLine(edit.getEndA());
						break;
					} else {
						bugInfo.setEditType(EditType.NO_CHANGE);
					}
				}
				System.out.println(bugInfo.getBugInstance().getBugPattern().getType());
				System.out.println(bugInfo.getEditType());
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
		return edits;
	}

	public void display() {
		if (edits != null) {
			for (Edit edit : edits) {
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
