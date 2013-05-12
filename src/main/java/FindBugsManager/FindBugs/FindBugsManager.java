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
	private File file = null;

	private static FindBugsManager instance = new FindBugsManager();

	private ArrayList<BugInfo> infoList = new ArrayList<BugInfo>();
	private ArrayList<BugInfo> preInfoList = new ArrayList<BugInfo>();

	private ArrayList<BugInfo> editedBugList = new ArrayList<BugInfo>();

	private FindBugsManager() {

	}

	public void createBugInfoList(File currentFile) {
		this.file = currentFile;
		if (file.length() == 0) {
			// nothing
		} else {
			infoList = new ArrayList<BugInfo>();
			parseXML(infoList);
		}
	}

	public void createPreBugInfoList(File previousFile) {
		this.file = previousFile;
		if (file.length() == 0) {
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
			Document doc = builder.parse(file);

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

	public void checkEditedBugs(DiffManager diff, BlameManager blame) {
		editedBugList = new ArrayList<BugInfo>();

		for (BugInfo bugInfo : preInfoList) {
			EditType type = bugInfo.getEditType();
			if (type == EditType.EDIT) {
				editedBugList.add(bugInfo);
			}
		}

		if (!editedBugList.isEmpty()) {
			int preBugStart, preBugEnd;
			for (BugInfo bugInfo : editedBugList) {
				preBugStart = bugInfo.getStartLine();
				preBugEnd = bugInfo.getEndLine();
				for (BugInfo info : infoList) {
					if (info.getPreStartLine() <= preBugStart
							&& preBugStart <= info.getPreEndLine()) {
						if (info.getBugInstance().equals(bugInfo.getBugInstance())) {
							info.setExistFlag();
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

	public void display() {
		BugInstance instance = null;
		int start = 0, end = 0;
		EditType type = null;
		for (BugInfo info : editedBugList) {
			instance = info.getBugInstance();
			start = info.getStartLine();
			end = info.getEndLine();
			type = info.getEditType();

			displayPatternInfo(instance);
			System.out.println("Amender : " + info.getAuthor());
			System.out.println("Line : " + start + " to " + end);
			System.out.println("EditType : " + type.name());
			System.out.println();
		}
		System.out.println("------------------------------------");
	}

	public void displayAll() {
		BugInstance instance = null;
		int start = 0, end = 0;

		int count = infoList.size();
		System.out.println("Number of Bugs : " + count + "\n");
		for (BugInfo info : infoList) {
			instance = info.getBugInstance();
			start = info.getStartLine();
			end = info.getEndLine();

			displayPatternInfo(instance);
			System.out.println("Author : " + info.getAuthor());
			System.out.println("Line : " + start + " to " + end);
			System.out.println();
		}
	}

	public void displayPatternInfo(BugInstance instance) {
		int bugRank = instance.getBugRank();
		String rankCategory = instance.getBugRankCategory().name();
		String bugPriority = instance.getPriorityString();
		String bugCategory = instance.getBugPattern().getCategory();
		String bugAbbrev = instance.getAbbrev();
		String bugType = instance.getType();

		System.out.println(bugCategory);
		System.out.println(bugAbbrev + " : " + bugType);
		System.out.println(bugPriority + " : " + rankCategory + "(" + bugRank + ")");
	}

}
