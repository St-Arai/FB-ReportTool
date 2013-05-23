package FindBugsManager.Core;

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

import FindBugsManager.UI.BugData;

public class XMLReader {

	private ArrayList<BugData> fixed = new ArrayList<BugData>();
	private ArrayList<BugData> previous = new ArrayList<BugData>();

	private String _filepath = "../bugOutput/Comparisons/bugData.xml";

	private DocumentBuilderFactory _factory = DocumentBuilderFactory.newInstance();
	private DocumentBuilder builder = null;

	private static XMLReader instance = new XMLReader();

	private XMLReader() {
		createBugLists();
	}

	public static XMLReader getInstance() {
		return instance;
	}

	private void createBugLists() {
		try {
			builder = _factory.newDocumentBuilder();
			Document doc = builder.parse(_filepath);

			Element root = doc.getDocumentElement();
			NodeList children = root.getChildNodes();

			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				if (child instanceof Element) {
					Element childElement = (Element) child;
					String tagName = childElement.getTagName();
					if (tagName.equals("FixedBugs")) {
						NodeList grandChild = childElement.getChildNodes();
						for (int j = 0; j < grandChild.getLength(); j++) {
							Node grand = grandChild.item(j);
							if (grand instanceof Element) {
								Element grandElement = (Element) grand;
								getElements(grandElement, fixed);
							}
						}
					} else if (tagName.equals("PreviousBugs")) {
						NodeList grandChild = childElement.getChildNodes();
						for (int j = 0; j < grandChild.getLength(); j++) {
							Node grand = grandChild.item(j);
							if (grand instanceof Element) {
								Element grandElement = (Element) grand;
								getElements(grandElement, previous);
							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	private void getElements(Element grandElement, ArrayList<BugData> list) {
		String bugCategory = null;
		String bugAbbrev = null;
		String bugType = null;
		String bugRank = null;
		String bugPoint = null;
		String bugPriority = null;
		String bugLine = null;
		String bugFixer = null;
		String bugAuthor = null;

		if (grandElement.getTagName().equals("BugInstance")) {
			NodeList greatChild = grandElement.getChildNodes();
			for (int k = 0; k < greatChild.getLength(); k++) {
				Node great = greatChild.item(k);
				if (great instanceof Element) {
					Element greatElement = (Element) great;
					String tagName = greatElement.getTagName();
					if (tagName.equals("Category")) {
						bugCategory = greatElement.getTextContent();
					} else if (tagName.equals("Abbreviation")) {
						bugAbbrev = greatElement.getTextContent();
					} else if (tagName.equals("Type")) {
						bugType = greatElement.getTextContent();
					} else if (tagName.equals("Rank")) {
						bugRank = greatElement.getTextContent();
					} else if (tagName.equals("Point")) {
						bugPoint = greatElement.getTextContent();
					} else if (tagName.equals("Line")) {
						bugLine = greatElement.getTextContent();
					} else if (tagName.equals("Priority")) {
						bugPriority = greatElement.getTextContent();
					} else if (tagName.equals("Amender")) {
						bugFixer = greatElement.getTextContent();
					} else if (tagName.equals("Author")) {
						bugAuthor = greatElement.getTextContent();
					} else {
						//
					}
				}
			}
			list.add(new BugData(bugCategory, bugAbbrev, bugType, bugRank, bugPoint, bugPriority,
					bugLine, bugFixer, bugAuthor));
		}
	}
	public ArrayList<BugData> getFixedBugDataList() {
		return fixed;
	}

	public ArrayList<BugData> getPreviousBugDataList() {
		return previous;
	}
}
