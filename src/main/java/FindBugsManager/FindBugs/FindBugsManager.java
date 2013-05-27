package FindBugsManager.FindBugs;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import FindBugs.DataSets.BugInstanceSet;
import FindBugsManager.Core.XMLReader;
import FindBugsManager.Git.BlameManager;
import FindBugsManager.Git.DiffManager;
import FindBugsManager.Git.EditType;

public class FindBugsManager {
	private File _file = null;

	private static FindBugsManager instance = new FindBugsManager();

	private XMLReader reader = new XMLReader();

	private ArrayList<BugInstanceSet> infoList = new ArrayList<BugInstanceSet>();
	private ArrayList<BugInstanceSet> preInfoList = new ArrayList<BugInstanceSet>();

	private ArrayList<BugInstanceSet> editedBugList = new ArrayList<BugInstanceSet>();

	private FindBugsManager() {

	}

	public void initBugInfoLists() {
		infoList = new ArrayList<BugInstanceSet>();
		preInfoList = new ArrayList<BugInstanceSet>();
		editedBugList = new ArrayList<BugInstanceSet>();
	}

	public static void runFindbugs(String selectedComment, String targetPath, File bugDataDirectory) {
		Runtime rt = Runtime.getRuntime();
		Process p = null;
		try {
			p = rt.exec(new String[]{"cmd.exe", "/C", "findbugs", "-textui", "-low", "-xml",
					"-output", selectedComment + ".xml", targetPath}, null, bugDataDirectory);
			p.waitFor();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void createBugInfoList(File currentFile) {
		_file = currentFile;
		if (_file.length() == 0) {
			// nothing
		} else {
			infoList = reader.parseFindBugsXML(infoList, _file);
		}
	}

	public void createPreBugInfoList(File previousFile) {
		_file = previousFile;
		if (_file.length() == 0) {
			// nothing
		} else {
			preInfoList = reader.parseFindBugsXML(preInfoList, _file);
		}
	}

	public void checkEditedBugs(DiffManager diff, BlameManager blame) {
		editedBugList = new ArrayList<BugInstanceSet>();

		for (BugInstanceSet bugInfo : preInfoList) {
			EditType type = bugInfo.getEditType();
			if (type == EditType.EDIT) {
				editedBugList.add(bugInfo);
			}
		}

		if (!editedBugList.isEmpty()) {
			int editedBugStart = 0;
			// int preBugEnd = 0;
			for (BugInstanceSet editedBugInfo : editedBugList) {
				editedBugStart = editedBugInfo.getStartLine();
				// preBugEnd = bugInfo.getEndLine();
				for (BugInstanceSet info : infoList) {
					if (info.getEditedStartLine() <= editedBugStart
							&& editedBugStart <= info.getEditedEndLine()) {
						if (info.getBugInstance().equals(editedBugInfo.getBugInstance())) {
							info.setExistFlag(true);
						}
					}
				}
			}

			int i = 0;
			for (BugInstanceSet info : editedBugList) {
				if (info.getExistFlag() == true) {
					editedBugList.remove(i);
				}
				i++;
			}

			ArrayList<String> author;
			for (BugInstanceSet bugInfo : editedBugList) {
				author = blame.getAuthors(bugInfo.getStartLine(), bugInfo.getEndLine());
				bugInfo.setAuthor(author.get(0));
			}
		}

		Collections.sort(editedBugList, new IndexSort());
		Collections.sort(infoList, new IndexSort());
	}

	public static FindBugsManager getInstance() {
		return instance;
	}

	public ArrayList<BugInstanceSet> getBugInfoList() {
		return infoList;
	}

	public ArrayList<BugInstanceSet> getPreBugInfoList() {
		return preInfoList;
	}

	public ArrayList<BugInstanceSet> getEditedBugList() {
		return editedBugList;
	}

	public int getBugCounts() {
		return infoList.size();
	}

	public int getPreBugCounts() {
		return preInfoList.size();
	}

	public void display() {
		for (BugInstanceSet info : infoList) {
			System.out.println(info.getBugInstance().getBugPattern().getType());
			System.out.println(info.getStartLine() + " ~ " + info.getEndLine());
			System.out.println(info.getEditType());
		}
	}

}

class IndexSort implements Comparator<BugInstanceSet>, Serializable {
	private static final long serialVersionUID = 1L;

	public int compare(BugInstanceSet set1, BugInstanceSet set2) {
		int rank1 = set1.getBugInstance().getBugRank();
		int rank2 = set2.getBugInstance().getBugRank();
		if (rank1 > rank2) {
			return 1;
		} else if (rank1 == rank2) {
			return 0;
		} else {
			return -1;
		}
	}
}
