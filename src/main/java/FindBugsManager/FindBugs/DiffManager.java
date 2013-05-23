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

public class DiffManager {

	private transient FindBugsManager manager = FindBugsManager.getInstance();

	private Collection<Edit> edits = null;
	private transient ArrayList<BugInfo> preInfoList = manager.getPreBugInfoList();;

	private final static String branchName = "master";

	private File _file = null;
	private String _path = null;

	public DiffManager(File file, String path) {
		_file = file;
		_path = path;

		// diffDriver();
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

		setPreviousInfo();
	}

	private void setPreviousInfo() {
		int preBugLine = 0;
		int editStartLine = 0;
		int editEndLine = 0;

		if (edits != null) {
			for (BugInfo preInfo : preInfoList) {
				for (Edit edit : edits) {
					preBugLine = preInfo.getStartLine();
					editStartLine = edit.getBeginA();
					editEndLine = edit.getEndA();
					if (editStartLine <= preBugLine && preBugLine <= editEndLine) {
						preInfo.setEditType(changeEnumType(edit.getType()));
						preInfo.setEditedStartLine(edit.getBeginB());
						preInfo.setEditedEndLine(edit.getEndB());
						break;
					} else {
						preInfo.setEditType(EditType.NO_CHANGE);
					}
				}
				System.out.println(preInfo.getBugInstance().getBugPattern().getType());
				System.out.println(preInfo.getEditType() + "\n");
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
