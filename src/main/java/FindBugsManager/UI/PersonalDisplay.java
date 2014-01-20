package FindBugsManager.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import FindBugsManager.Core.XMLReader;
import FindBugsManager.DataSets.BugData;
import FindBugsManager.DataSets.PersonalData;
import FindBugsManager.Git.AccountManager;

public class PersonalDisplay {

	private JFrame _frame = null;

	public PersonalDisplay(JFrame mainFrame, final String targetName) {
		_frame = mainFrame;

		_frame.setSize(new Dimension(1200, 750));
		_frame.setTitle("Personal Bug Info : " + targetName);
		_frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		AccountManager account = AccountManager.getInstance();

		PersonalData pData = null;
		pData = account.getPersonalData(targetName);

		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
		leftPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.PAGE_AXIS));

		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));
		JLabel fbLabel = new JLabel();
		ImageIcon fbIcon = new ImageIcon("./img/findbugs.png");
		fbLabel.setIcon(fbIcon);
		rightPanel.add(fbLabel);

		JScrollPane scrollpaneL = new JScrollPane(leftPanel);
		JScrollPane scrollpaneR = new JScrollPane(rightPanel);
		JScrollPane scrollpaneC = new JScrollPane(centerPanel);
		JSplitPane splitpaneR = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitpaneR.setDividerSize(3);
		final JSplitPane splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitpane.setDividerSize(3);

		JButton updateButton = new JButton("Update");
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_frame.remove(splitpane);
				new PersonalDisplay(_frame, targetName);
			}
		});
		leftPanel.add(updateButton);

		XMLReader bugData = new XMLReader();
		bugData.createLatestBugLists();
		bugData.createAllBugLists();
		ArrayList<BugData> fixedData = bugData.getFixedBugDataList();
		ArrayList<BugData> remainData = bugData.getRemainBugDataList();

		JPanel scorePanel = new JPanel();
		scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.PAGE_AXIS));
		scorePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		JPanel fixedPanel = new JPanel();
		fixedPanel.setLayout(new BoxLayout(fixedPanel, BoxLayout.PAGE_AXIS));
		fixedPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel iconLabel = new JLabel();
		ImageIcon icon = new ImageIcon("./img/twitter_icon.jpg");
		iconLabel.setIcon(icon);
		iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel nameLabel = new JLabel();
		nameLabel.setText(pData.getName());
		nameLabel.setFont(new Font("Consolas", Font.BOLD, 30));
		nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel jobLabel = new JLabel();
		ImageIcon remodeler = new ImageIcon("./img/remodeler.png");
		jobLabel.setIcon(remodeler);
		jobLabel.setText(pData.getJob() + " Lv." + pData.getLevel());
		jobLabel.setFont(new Font("Consolas", Font.BOLD, 20));
		jobLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		jobLabel.setBackground(Color.white);

		JLabel text = new JLabel("Fixed Bugs");
		text.setFont(new Font("Consolas", Font.BOLD, 30));
		text.setForeground(Color.BLUE);
		text.setHorizontalAlignment(JLabel.LEFT);
		text.setVerticalAlignment(JLabel.TOP);
		fixedPanel.add(text);

		int sum = 0;
		int corr = 0;
		int sec = 0;
		int bad = 0;
		int style = 0;
		int per = 0;
		int mal = 0;
		int mtc = 0;
		int i18 = 0;
		int other = 0;

		for (BugData data : fixedData) {
			String fixer = data.getFixer();
			if (fixer.equals(targetName)) {
				sum += data.getPoint();
			}
		}

		ArrayList<BugData> pBugList = account.getPersonalBugDataList(targetName);
		for (BugData data : pBugList) {
			String category = data.getCategory();
			if (category.equals("CORRECTNESS")) {
				corr++;
			} else if (category.equals("SECURITY")) {
				sec++;
			} else if (category.equals("BAD_PRACTICE")) {
				bad++;
			} else if (category.equals("STYLE")) {
				style++;
			} else if (category.equals("PERFORMANCE")) {
				per++;
			} else if (category.equals("MALICIOUS_CODE")) {
				mal++;
			} else if (category.equals("MT_CORRECTNESS")) {
				mtc++;
			} else if (category.equals("I18N")) {
				i18++;
			} else {
				other++;
			}
		}

		JLabel corLabel = new JLabel("COR:" + String.valueOf(corr) + "  " + "SEC:"
				+ String.valueOf(sec));
		JLabel badLabel = new JLabel("BAD:" + String.valueOf(bad) + "  " + "STY:"
				+ String.valueOf(style));
		JLabel perLabel = new JLabel("PER:" + String.valueOf(per) + "  " + "MAL:"
				+ String.valueOf(mal));
		JLabel mtcLabel = new JLabel("MTC:" + String.valueOf(mtc) + "  " + "i18:"
				+ String.valueOf(i18));
		JLabel othLabel = new JLabel("Other:" + String.valueOf(other));

		rightPanel.add(corLabel);
		rightPanel.add(badLabel);
		rightPanel.add(perLabel);
		rightPanel.add(mtcLabel);
		rightPanel.add(othLabel);
		rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		JLabel remainText = new JLabel("Remaining Bugs");
		remainText.setFont(new Font("Consolas", Font.BOLD, 30));
		remainText.setForeground(Color.GREEN);
		JLabel bugCount = new JLabel(String.valueOf(remainData.size()));
		bugCount.setFont(new Font("Consolas", Font.BOLD, 20));
		bugCount.setForeground(Color.RED);
		rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		rightPanel.add(remainText);
		rightPanel.add(bugCount);
		rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));

		JLabel pointText = new JLabel();
		pointText.setFont(new Font("Consolas", Font.BOLD, 25));
		pointText.setForeground(Color.RED);
		pointText.setHorizontalAlignment(JLabel.LEFT);
		pointText.setVerticalAlignment(JLabel.TOP);
		pointText.setText("+ " + String.valueOf(sum) + " points!");
		fixedPanel.add(pointText);
		fixedPanel.add(Box.createRigidArea(new Dimension(0, 30)));

		for (BugData data : fixedData) {
			String fixer = data.getFixer();
			if (fixer.equals(pData.getName())) {
				int rank = data.getRank();
				String abbrev = data.getAbbrev();
				String category = data.getCategory();
				String priority = data.getPriority();
				String type = data.getType();
				String author = data.getFixer();

				int point = data.getPoint();

				JLabel label = new JLabel("Confidence : " + priority);
				JLabel label2 = new JLabel("Rank : " + rank);
				JLabel label3 = new JLabel("Category : " + category);
				JLabel label4 = new JLabel("Abbrev : " + abbrev);
				JLabel label5 = new JLabel("Type : " + type);
				JLabel label6 = new JLabel("Fixer :" + author);
				JLabel pointlabel = new JLabel("+ " + point + " points");

				int fontSize = 18;
				setFont(label, fontSize);
				setFont(label2, fontSize);
				setFont(label3, fontSize);
				setFont(label4, fontSize);
				setFont(label5, fontSize);
				setFont(label6, fontSize);
				setFont(pointlabel, fontSize);
				pointlabel.setForeground(Color.red);

				fixedPanel.add(label);
				fixedPanel.add(label2);
				fixedPanel.add(label3);
				fixedPanel.add(label4);
				fixedPanel.add(label5);
				fixedPanel.add(label6);
				fixedPanel.add(pointlabel);
				fixedPanel.add(Box.createRigidArea(new Dimension(0, 20)));
			}
		}

		int newBugCount = 0;
		for (BugData data : remainData) {
			int rank = data.getRank();
			String abbrev = data.getAbbrev();
			String category = data.getCategory();
			String priority = data.getPriority();
			String type = data.getType();
			String condition = data.getCondition();
			String line = data.getLine();

			int point = data.getPoint();

			JLabel label = new JLabel("Confidence : " + priority);
			JLabel label2 = new JLabel("Rank : " + rank);
			JLabel label3 = new JLabel("Category : " + category);
			JLabel label4 = new JLabel("Abbrev : " + abbrev);
			JLabel label5 = new JLabel("Type : " + type);
			JLabel label6 = new JLabel("Line : " + line);
			JLabel pointlabel = new JLabel("+ " + point + " points");

			int fontSize = 15;
			setFont(label, fontSize);
			setFont(label2, fontSize);
			setFont(label3, fontSize);
			setFont(label4, fontSize);
			setFont(label5, fontSize);
			setFont(label6, fontSize);
			setFont(pointlabel, fontSize);

			pointlabel.setForeground(Color.red);

			if (condition.equals("NEW")) {
				newBugCount++;
				JLabel label8 = new JLabel("New Bug!");
				label8.setFont(new Font("Consolas", Font.BOLD, 18));
				label8.setForeground(Color.MAGENTA);
				label8.setHorizontalAlignment(JLabel.LEFT);
				label8.setVerticalAlignment(JLabel.TOP);
				rightPanel.add(label8);
			}
			rightPanel.add(label);
			rightPanel.add(label2);
			rightPanel.add(label3);
			rightPanel.add(label4);
			rightPanel.add(label5);
			rightPanel.add(label6);
			rightPanel.add(pointlabel);
			rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		}
		JLabel newlabel = new JLabel(String.valueOf(newBugCount));
		setFont(newlabel, 20);
		rightPanel.add(newlabel, 9);

		int newPoint = pData.getTotalPoint();
		JLabel score = new JLabel("+ " + newPoint + " points!");
		score.setFont(new Font("Consolas", Font.BOLD, 30));
		score.setAlignmentX(Component.CENTER_ALIGNMENT);
		score.setForeground(Color.RED);
		int remain = pData.getRemain() - newPoint;
		JLabel next = new JLabel("next... " + remain + " points.");
		next.setFont(new Font("Consolas", Font.BOLD, 20));
		next.setAlignmentX(Component.CENTER_ALIGNMENT);

		scorePanel.add(score);
		scorePanel.add(next);

		JLabel revLabel = new JLabel("Revision History");
		revLabel.setFont(new Font("Consolas", Font.BOLD, 30));
		revLabel.setForeground(new Color(0, 191, 255));

		JLabel sumLabel = new JLabel("Total Score : "
				+ account.getPersonalData(targetName).getTotalPoint());
		setFont(sumLabel, 18);
		sumLabel.setForeground(Color.RED);

		leftPanel.add(revLabel);
		leftPanel.add(sumLabel);
		leftPanel.add(Box.createRigidArea(new Dimension(0, 30)));

		ArrayList<BugData> historyDataList = account.getPersonalBugDataList(targetName);
		Collections.sort(historyDataList, new IndexSort());

		for (BugData data : historyDataList) {
			int rank = data.getRank();
			String abbrev = data.getAbbrev();
			String category = data.getCategory();
			String priority = data.getPriority();
			String type = data.getType();

			int point = data.getPoint();

			JLabel label = new JLabel("Confidence : " + priority);
			JLabel label2 = new JLabel("Rank : " + rank);
			JLabel label3 = new JLabel("Category : " + category);
			JLabel label4 = new JLabel("Abbrev : " + abbrev);
			JLabel label5 = new JLabel("Type : " + type);
			JLabel pointlabel = new JLabel("+ " + point + " points");

			int fontSize = 15;
			setFont(label, fontSize);
			setFont(label2, fontSize);
			setFont(label3, fontSize);
			setFont(label4, fontSize);
			setFont(label5, fontSize);
			setFont(pointlabel, fontSize);

			leftPanel.add(label);
			leftPanel.add(label2);
			leftPanel.add(label3);
			leftPanel.add(label4);
			leftPanel.add(label5);
			leftPanel.add(pointlabel);

			pointlabel.setForeground(Color.red);

			leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		}

		centerPanel.add(fixedPanel);

		scrollpaneR.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollpaneR.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		scrollpaneC.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollpaneC.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		scrollpaneL.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollpaneL.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		splitpaneR.setDividerSize(3);
		splitpaneR.setRightComponent(scrollpaneR);
		splitpaneR.setLeftComponent(scrollpaneC);

		splitpaneR.setPreferredSize(null);
		splitpane.setRightComponent(splitpaneR);
		splitpane.setLeftComponent(scrollpaneL);

		splitpane.setPreferredSize(null);
		_frame.add(splitpane, BorderLayout.CENTER);
		_frame.setVisible(true);
	}

	private void setFont(JLabel label, int fontSize) {
		label.setFont(new Font("Consolas", Font.BOLD, fontSize));
		label.setHorizontalAlignment(JLabel.LEFT);
		label.setVerticalAlignment(JLabel.TOP);
	}

}
