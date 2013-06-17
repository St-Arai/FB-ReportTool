package FindBugsManager.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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

public class PersonalDisplay implements ActionListener {

	private JFrame _frame = null;

	private AccountManager account = AccountManager.getInstance();

	public PersonalDisplay(JFrame mainFrame, String targetName) {
		_frame = mainFrame;

		_frame.setSize(new Dimension(1000, 750));
		_frame.setTitle("Personal Bug Info : " + targetName);
		_frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

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

		JLabel bugInfo = new JLabel("Remaining Bugs");
		bugInfo.setFont(new Font("Consolas", Font.BOLD, 30));
		bugInfo.setForeground(Color.GREEN);
		rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		rightPanel.add(bugInfo);
		rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		JPanel scorePanel = new JPanel();
		scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.PAGE_AXIS));

		JPanel bugPanel = new JPanel();
		bugPanel.setLayout(new BoxLayout(bugPanel, BoxLayout.PAGE_AXIS));
		bugPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		JButton button = new JButton("Back");
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		button.setActionCommand("1");
		button.addActionListener(this);

		XMLReader bugData = new XMLReader();
		bugData.createBugLists();
		ArrayList<BugData> fixedData = bugData.getFixedBugDataList();
		ArrayList<BugData> remainData = bugData.getRemainBugDataList();

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
		bugPanel.add(text);

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

				bugPanel.add(label);
				bugPanel.add(label2);
				bugPanel.add(label3);
				bugPanel.add(label4);
				bugPanel.add(label5);
				bugPanel.add(label6);
				bugPanel.add(pointlabel);
				bugPanel.add(Box.createRigidArea(new Dimension(0, 20)));
			}
		}

		for (BugData data : remainData) {
			int rank = data.getRank();
			String abbrev = data.getAbbrev();
			String category = data.getCategory();
			String priority = data.getPriority();
			String type = data.getType();
			String condition = data.getCondition();
			String line = data.getLine();
			String author = data.getAuthor();

			int point = data.getPoint();

			JLabel label = new JLabel("Confidence : " + priority);
			JLabel label2 = new JLabel("Rank : " + rank);
			JLabel label3 = new JLabel("Category : " + category);
			JLabel label4 = new JLabel("Abbrev : " + abbrev);
			JLabel label5 = new JLabel("Type : " + type);
			JLabel label6 = new JLabel("Line : " + line);
			JLabel label7 = new JLabel("Author :" + author);
			JLabel pointlabel = new JLabel("+ " + point + " points");

			int fontSize = 15;
			setFont(label, fontSize);
			setFont(label2, fontSize);
			setFont(label3, fontSize);
			setFont(label4, fontSize);
			setFont(label5, fontSize);
			setFont(label6, fontSize);
			setFont(label7, fontSize);
			setFont(pointlabel, fontSize);

			pointlabel.setForeground(Color.red);

			if (condition.equals("NEW")) {
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
			rightPanel.add(label7);
			rightPanel.add(pointlabel);
			rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		}

		int newPoint = pData.getPoint();
		JLabel score = new JLabel("+ " + newPoint + " points!");
		score.setFont(new Font("Consolas", Font.BOLD, 30));
		score.setAlignmentX(Component.CENTER_ALIGNMENT);
		score.setForeground(Color.RED);
		int remain = pData.getRemain() - newPoint;
		JLabel next = new JLabel("next... " + remain + " points.");
		next.setFont(new Font("Consolas", Font.BOLD, 20));
		next.setAlignmentX(Component.CENTER_ALIGNMENT);
		//
		// DefaultPieDataset piedata = new DefaultPieDataset();
		// piedata.setValue("CORRECTNESS", 10);
		// piedata.setValue("BAD PRACTICE", 8);
		// piedata.setValue("DODGYCODE", 15);
		// piedata.setValue("PERFORMANCE", 1);
		// JFreeChart chart = ChartFactory.createPieChart("Fixed Bug Category", piedata, false,
		// false,
		// false);
		//
		// chart.getTitle().setFont(new Font("Consolas", Font.BOLD, 8));
		//
		// PiePlot plot = (PiePlot) chart.getPlot();
		// plot.setOutlineVisible(false);
		//
		// plot.setShadowPaint(null);
		// plot.setSimpleLabels(true);
		// plot.setLabelFont(new Font("Consolas", Font.BOLD, 4));
		// plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}={1}"));
		// plot.setLabelBackgroundPaint(null);
		// plot.setLabelOutlineStroke(null);
		// plot.setLabelShadowPaint(null);
		// plot.setExplodePercent(piedata.getKey(piedata.getItemCount() - 1), 0.1);
		//
		// ChartPanel cpanel = new ChartPanel(chart, 50, 50, 50, 50, 100, 100, true, false, false,
		// false, true, false);
		// // cpanel.setBounds(10, 10, 50, 50);

		scorePanel.add(score);
		scorePanel.add(next);

		JLabel revLabel = new JLabel("Revision History");
		revLabel.setFont(new Font("Consolas", Font.BOLD, 30));
		revLabel.setForeground(new Color(0, 191, 255));

		JLabel sumLabel = new JLabel("Total Points : "
				+ account.getPersonalData(targetName).getPoint());
		setFont(sumLabel, 18);
		sumLabel.setForeground(Color.RED);

		leftPanel.add(revLabel);
		leftPanel.add(sumLabel);
		leftPanel.add(Box.createRigidArea(new Dimension(0, 30)));

		ArrayList<BugData> dataList = account.getPersonalBugData(targetName);
		for (BugData data : dataList) {
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
		// leftPanel.add(iconLabel);
		// leftPanel.add(nameLabel);
		// leftPanel.add(jobLabel);
		// leftPanel.add(Box.createRigidArea(new Dimension(0, 50)));
		// leftPanel.add(scorePanel);
		// leftPanel.add(Box.createRigidArea(new Dimension(0, 50)));
		// leftPanel.add(button);

		centerPanel.add(bugPanel);
		// panel.add(cpanel);

		JScrollPane scrollpaneR = new JScrollPane(rightPanel);
		scrollpaneR.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollpaneR.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		JScrollPane scrollpaneC = new JScrollPane(centerPanel);
		scrollpaneC.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollpaneC.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		JScrollPane scrollpaneL = new JScrollPane(leftPanel);
		scrollpaneL.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollpaneL.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		JSplitPane splitpaneR = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitpaneR.setDividerSize(3);
		splitpaneR.setRightComponent(scrollpaneR);
		splitpaneR.setLeftComponent(scrollpaneC);
		// splitpaneR.setResizeWeight(1.0);

		JSplitPane splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitpane.setDividerSize(5);

		splitpane.setRightComponent(splitpaneR);
		splitpane.setLeftComponent(scrollpaneL);

		// JFreeChart chart = ChartFactory.createPieChart("Bug Category",
		// piedata,
		// true, false, false);

		// ChartPanel cpanel = new ChartPanel(chart);
		// cpanel.setAlignmentX(0.5f);
		// panel.add(cpanel);

		_frame.add(splitpane, BorderLayout.CENTER);
		_frame.setVisible(true);
	}
	private void setFont(JLabel label, int fontSize) {
		label.setFont(new Font("Consolas", Font.BOLD, fontSize));
		label.setHorizontalAlignment(JLabel.LEFT);
		label.setVerticalAlignment(JLabel.TOP);
	}

	public void actionPerformed(ActionEvent e) {
		int command = Integer.parseInt(e.getActionCommand());
		switch (command) {
			case 1 :
				_frame.getContentPane().removeAll();
				new GitScanning(_frame);
				break;
			default :
				break;
		}
	}
}
