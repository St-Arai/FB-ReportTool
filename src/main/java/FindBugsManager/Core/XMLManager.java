package FindBugsManager.Core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import FindBugsManager.DataSets.BugInstanceSet;
import FindBugsManager.FindBugs.FindBugsManager;

public class XMLManager {

	public XMLManager() {

	}

	private String bugOutputPath = Settings.getOutputPath();
	private final File bugOutputDirectory = new File(bugOutputPath);

	public void createXML(FindBugsManager manager, String id, int bonus, String targetCateg,
			int categBonus) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();

			Document document = builder.newDocument();

			Element project = document.createElement("Project");
			document.appendChild(project);

			Element fixedBugs = document.createElement("FixedBugs");
			project.appendChild(fixedBugs);
			Element fixedNumber = document.createElement("BugNumber");
			fixedBugs.appendChild(fixedNumber);

			Element remainBugs = document.createElement("RemainingBugs");
			project.appendChild(remainBugs);
			Element number = document.createElement("BugNumber");
			remainBugs.appendChild(number);

			ArrayList<BugInstanceSet> editedBugs = manager.getEditedBugList();
			String fixNum = String.valueOf(editedBugs.size());
			Text fixedNumberText = document.createTextNode(fixNum);
			fixedNumber.appendChild(fixedNumberText);

			for (BugInstanceSet info : editedBugs) {
				Element instance = document.createElement("BugInstance");
				fixedBugs.appendChild(instance);

				createNodes(document, info, instance, bonus, targetCateg, categBonus, "Edit");

				Element amender = document.createElement("Amender");
				instance.appendChild(amender);
				Text amenderText = null;
				if (info.getEditType().name().equals("NEW")) {
					amenderText = document.createTextNode(info.getAuthor());
				} else {
					amenderText = document.createTextNode(info.getAmender());
				}
				amender.appendChild(amenderText);
			}

			ArrayList<BugInstanceSet> bugInfo = manager.getBugInfoList();
			String num = String.valueOf(bugInfo.size());
			Text numberText = document.createTextNode(num);
			number.appendChild(numberText);

			for (BugInstanceSet info : bugInfo) {
				Element instance = document.createElement("BugInstance");
				remainBugs.appendChild(instance);

				createNodes(document, info, instance, 1, null, 1, "Remain");

				Element author = document.createElement("Author");
				instance.appendChild(author);
				Text authorText = document.createTextNode(info.getAuthor());
				author.appendChild(authorText);
			}

			TransformerFactory tf = TransformerFactory.newInstance();

			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(
					org.apache.xml.serializer.OutputPropertiesFactory.S_KEY_INDENT_AMOUNT, "2");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");

			DOMSource source = new DOMSource(document);
			xmlOutput("bugData" + id, transformer, source);
			xmlOutput("bugData", transformer, source);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void xmlOutput(String fileName, Transformer transformer, DOMSource source)
			throws TransformerException, IOException {
		File newXML = new File(bugOutputDirectory, fileName + ".xml");
		FileOutputStream os = new FileOutputStream(newXML);
		StreamResult result = new StreamResult(os);
		transformer.transform(source, result);
		os.close();
	}

	private void createNodes(Document document, BugInstanceSet info, Element instance, int bonus,
			String targetCateg, int categBonus, String bugstype) {
		Element category = document.createElement("Category");
		instance.appendChild(category);
		Text categoryText = document.createTextNode(info.getBugInstance().getBugPattern()
				.getCategory());
		category.appendChild(categoryText);

		Element abbrev = document.createElement("Abbreviation");
		instance.appendChild(abbrev);
		Text abbrevText = document.createTextNode(info.getBugInstance().getAbbrev());
		abbrev.appendChild(abbrevText);

		Element type = document.createElement("Type");
		instance.appendChild(type);
		Text bugTypeText = document.createTextNode(info.getBugInstance().getType());
		type.appendChild(bugTypeText);

		Element rank = document.createElement("Rank");
		instance.appendChild(rank);
		Text rankText = document.createTextNode(String.valueOf(info.getBugInstance().getBugRank()));
		rank.appendChild(rankText);

		Element point = document.createElement("Point");
		instance.appendChild(point);

		String categ = info.getBugInstance().getBugPattern().getCategory();
		Text pointText = null;
		if (bugstype.equals("Edit") && info.getEditType().name().equals("NEW")) {
			pointText = document.createTextNode((String.valueOf((21 - info.getBugInstance()
					.getBugRank()) * -1)));
		} else {
			if (categ.equals(targetCateg)) {
				pointText = document.createTextNode(String.valueOf((21 - info.getBugInstance()
						.getBugRank()) * (bonus * categBonus)));
			} else {
				pointText = document.createTextNode(String.valueOf((21 - info.getBugInstance()
						.getBugRank()) * bonus));
			}
		}
		point.appendChild(pointText);

		Element priority = document.createElement("Priority");
		instance.appendChild(priority);
		Text priorityText = null;
		String priorityString = info.getBugInstance().getPriorityString();
		if (priorityString.equals("優先度(高)")) {
			priorityText = document.createTextNode("High");
		} else if (priorityString.equals("優先度(中)")) {
			priorityText = document.createTextNode("Middle");
		} else {
			priorityText = document.createTextNode("Low");
		}
		priority.appendChild(priorityText);

		Element condition = document.createElement("Condition");
		instance.appendChild(condition);
		Text conditionText = document.createTextNode(String.valueOf(info.getEditType()));
		condition.appendChild(conditionText);

		Element line = document.createElement("Line");
		instance.appendChild(line);
		Text lineText = document.createTextNode(String.valueOf(info.getStartLine()) + " ~ "
				+ String.valueOf(info.getEndLine()));
		line.appendChild(lineText);
	}
}
