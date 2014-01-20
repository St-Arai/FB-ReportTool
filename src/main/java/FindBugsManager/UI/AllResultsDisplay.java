package FindBugsManager.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import FindBugsManager.DataSets.BugData;
import FindBugsManager.DataSets.PersonalData;
import FindBugsManager.Git.AccountManager;

public class AllResultsDisplay {

	private JFrame _frame = null;

	public AllResultsDisplay(JFrame frame) {
		_frame = frame;

		_frame.setSize(new Dimension(1200, 750));
		_frame.setTitle("Results");
		_frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		AccountManager account = AccountManager.getInstance();
		ArrayList<String> nameList = account.getNameList();

		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
		leftPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.PAGE_AXIS));
		rightPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		JPanel centerRightPanel = new JPanel();
		centerRightPanel.setLayout(new BoxLayout(centerRightPanel, BoxLayout.PAGE_AXIS));
		centerRightPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		JPanel centerLeftPanel = new JPanel();
		centerLeftPanel.setLayout(new BoxLayout(centerLeftPanel, BoxLayout.PAGE_AXIS));
		centerLeftPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		JScrollPane scrollpaneL = new JScrollPane(leftPanel);
		scrollpaneL.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollpaneL.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		JScrollPane scrollpaneR = new JScrollPane(rightPanel);
		scrollpaneR.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollpaneR.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		JScrollPane scrollpaneRC = new JScrollPane(centerRightPanel);
		scrollpaneRC.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollpaneRC.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		JScrollPane scrollpaneLC = new JScrollPane(centerLeftPanel);
		scrollpaneLC.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollpaneLC.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		JSplitPane splitpaneR = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitpaneR.setDividerSize(3);
		splitpaneR.setRightComponent(scrollpaneR);
		splitpaneR.setLeftComponent(scrollpaneRC);
		splitpaneR.setPreferredSize(null);

		JSplitPane splitpaneL = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitpaneL.setDividerSize(3);
		splitpaneL.setRightComponent(scrollpaneLC);
		splitpaneL.setLeftComponent(scrollpaneL);
		splitpaneL.setPreferredSize(null);

		final JSplitPane splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitpane.setDividerSize(3);
		splitpane.setRightComponent(splitpaneR);
		splitpane.setLeftComponent(splitpaneL);
		splitpane.setPreferredSize(null);

		JButton button = new JButton("Update");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_frame.remove(splitpane);
				new AllResultsDisplay(_frame);
			}
		});
		leftPanel.add(button);

		ArrayList<PersonalData> dataList = account.getPersonalDataList();
		int fontSize = 18;
		for (int i = 0; i < nameList.size(); i++) {
			PersonalData pData = dataList.get(i);
			String fixer = pData.getName();

			JLabel nameLabel = new JLabel(fixer);
			nameLabel.setFont(new Font("Consolas", Font.BOLD, 30));
			nameLabel.setHorizontalAlignment(JLabel.LEFT);
			nameLabel.setVerticalAlignment(JLabel.TOP);

			int sum = account.getPersonalData(fixer).getTotalPoint();
			JLabel pointText = new JLabel();
			pointText.setFont(new Font("Consolas", Font.BOLD, 25));
			pointText.setForeground(Color.RED);
			pointText.setHorizontalAlignment(JLabel.LEFT);
			pointText.setVerticalAlignment(JLabel.TOP);
			pointText.setText("+ " + String.valueOf(sum) + " points!");

			if (i == 0) {
				nameLabel.setForeground(Color.BLUE);
				leftPanel.add(nameLabel);
				leftPanel.add(pointText);
				leftPanel.add(Box.createRigidArea(new Dimension(0, 30)));
			} else if (i == 1) {
				nameLabel.setForeground(Color.GREEN);
				centerLeftPanel.add(nameLabel);
				centerLeftPanel.add(pointText);
				centerLeftPanel.add(Box.createRigidArea(new Dimension(0, 30)));
			} else if (i == 2) {
				nameLabel.setForeground(Color.ORANGE);
				centerRightPanel.add(nameLabel);
				centerRightPanel.add(pointText);
				centerRightPanel.add(Box.createRigidArea(new Dimension(0, 30)));
			} else if (i == 3) {
				nameLabel.setForeground(Color.PINK);
				rightPanel.add(nameLabel);
				rightPanel.add(pointText);
				rightPanel.add(Box.createRigidArea(new Dimension(0, 30)));
			} else {
				System.out.println("No!");
			}

			ArrayList<BugData> bugDataList = account.getPersonalBugDataList(fixer);

			Collections.sort(bugDataList, new IndexSort());

			for (BugData bugData : bugDataList) {
				int rank = bugData.getRank();
				String abbrev = bugData.getAbbrev();
				String category = bugData.getCategory();
				String priority = bugData.getPriority();
				String type = bugData.getType();

				int point = bugData.getPoint();

				JLabel label = new JLabel("Confidence : " + priority);
				JLabel label2 = new JLabel("Rank : " + rank);
				JLabel label3 = new JLabel("Category : " + category);
				JLabel label4 = new JLabel("Abbrev : " + abbrev);
				JLabel label5 = new JLabel("Type : " + type);
				JLabel pointlabel = new JLabel("+ " + point + " points");

				setFont(label, fontSize);
				setFont(label2, fontSize);
				setFont(label3, fontSize);
				setFont(label4, fontSize);
				setFont(label5, fontSize);
				setFont(pointlabel, fontSize);
				pointlabel.setForeground(Color.red);

				if (i == 0) {
					leftPanel.add(label);
					leftPanel.add(label2);
					leftPanel.add(label3);
					leftPanel.add(label4);
					leftPanel.add(label5);
					leftPanel.add(pointlabel);
					leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));
				} else if (i == 1) {
					centerLeftPanel.add(label);
					centerLeftPanel.add(label2);
					centerLeftPanel.add(label3);
					centerLeftPanel.add(label4);
					centerLeftPanel.add(label5);
					centerLeftPanel.add(pointlabel);
					centerLeftPanel.add(Box.createRigidArea(new Dimension(0, 20)));

				} else if (i == 2) {
					centerRightPanel.add(label);
					centerRightPanel.add(label2);
					centerRightPanel.add(label3);
					centerRightPanel.add(label4);
					centerRightPanel.add(label5);
					centerRightPanel.add(pointlabel);
					centerRightPanel.add(Box.createRigidArea(new Dimension(0, 20)));
				} else if (i == 3) {
					rightPanel.add(label);
					rightPanel.add(label2);
					rightPanel.add(label3);
					rightPanel.add(label4);
					rightPanel.add(label5);
					rightPanel.add(pointlabel);
					rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));

				} else {
					System.out.println("NO!");
				}
			}
		}

		_frame.add(splitpane, BorderLayout.CENTER);

		_frame.setVisible(true);
	}

	private void setFont(JLabel label, int fontSize) {
		label.setFont(new Font("Consolas", Font.BOLD, fontSize));
		label.setHorizontalAlignment(JLabel.LEFT);
		label.setVerticalAlignment(JLabel.TOP);
	}
}

class IndexSort implements Comparator<BugData>, Serializable {
	private static final long serialVersionUID = 1L;

	public int compare(BugData data1, BugData data2) {
		int rank1 = data1.getPoint();
		int rank2 = data2.getPoint();
		if (rank1 < rank2) {
			return 1;
		} else if (rank1 == rank2) {
			return 0;
		} else {
			return -1;
		}
	}
}
