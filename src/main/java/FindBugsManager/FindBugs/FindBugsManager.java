package FindBugsManager.FindBugs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.umd.cs.findbugs.BugInstance;

public class FindBugsManager {
	private File _file = null;

	private static FindBugsManager instance = new FindBugsManager();

	private ArrayList<BugInfo> infoList = new ArrayList<BugInfo>();
	private ArrayList<BugInfo> preInfoList = new ArrayList<BugInfo>();

	private ArrayList<BugInfo> editedBugList = new ArrayList<BugInfo>();

	private FindBugsManager() {

	}

	public void createBugInfoList(File currentFile) {
		_file = currentFile;
		if (_file.length() == 0) {
			// nothing
		} else {
			infoList = new ArrayList<BugInfo>();
			parseXML(infoList);
		}
	}

	public void createPreBugInfoList(File previousFile) {
		_file = previousFile;
		if (_file.length() == 0) {
			// nothing
		} else {
			preInfoList = new ArrayList<BugInfo>();
			parseXML(preInfoList);
		}
	}

	private void parseXML(ArrayList<BugInfo> bugInfoList) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;

		int startLine = 0, endLine = 0;

		try {
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse(_file);

			Element root = doc.getDocumentElement();
			NodeList children = root.getChildNodes();

			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				if (child instanceof Element) {
					Element childElement = (Element) child;
					if (childElement.getTagName().equals("BugInstance")) {
						int bugPriority = Integer.parseInt(childElement.getAttribute("priority"));
						String bugType = childElement.getAttribute("type");

						NodeList grandChild = childElement.getChildNodes();
						for (int j = 0; j < grandChild.getLength(); j++) {
							Node grand = grandChild.item(j);
							if (grand instanceof Element) {
								Element grandElement = (Element) grand;
								if (grandElement.getTagName().equals("SourceLine")) {
									startLine = Integer
											.parseInt(grandElement.getAttribute("start"));
									endLine = Integer.parseInt(grandElement.getAttribute("end"));
								}
							}
						}
						BugInstance instance = new BugInstance(bugType, bugPriority);

						BugInfo info = new BugInfo(instance, startLine, endLine);
						bugInfoList.add(info);
					}
				}
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void checkEditedBugs(DiffManager diff, BlameManager blame) {
		editedBugList = new ArrayList<BugInfo>();

		for (BugInfo bugInfo : preInfoList) {
			EditType type = bugInfo.getEditType();
			if (type == EditType.EDIT) {
				editedBugList.add(bugInfo);
			}
		}

		if (!editedBugList.isEmpty()) {
			int editedBugStart = 0;
			// int preBugEnd = 0;
			for (BugInfo editedBugInfo : editedBugList) {
				editedBugStart = editedBugInfo.getStartLine();
				// preBugEnd = bugInfo.getEndLine();
				for (BugInfo info : infoList) {
					if (info.getEditedStartLine() <= editedBugStart
							&& editedBugStart <= info.getEditedEndLine()) {
						if (info.getBugInstance().equals(editedBugInfo.getBugInstance())) {
							info.setExistFlag(true);
						}
					}
				}
			}

			int i = 0;
			for (BugInfo info : editedBugList) {
				if (info.getExistFlag() == true) {
					editedBugList.remove(i);
				}
				i++;
			}

			ArrayList<String> author;
			for (BugInfo bugInfo : editedBugList) {
				author = blame.getAuthors(bugInfo.getStartLine(), bugInfo.getEndLine());
				bugInfo.setAuthor(author.get(0));
			}
		}
	}

	public static FindBugsManager getInstance() {
		return instance;
	}

	public ArrayList<BugInfo> getBugInfoList() {
		return infoList;
	}

	public ArrayList<BugInfo> getPreBugInfoList() {
		return preInfoList;
	}

	public ArrayList<BugInfo> getEditedBugList() {
		return editedBugList;
	}

	public int getBugCounts() {
		return infoList.size();
	}

	public int getPreBugCounts() {
		return preInfoList.size();
	}

	public void display() {
		System.out.println("FindBugsManager.");
	}

}
