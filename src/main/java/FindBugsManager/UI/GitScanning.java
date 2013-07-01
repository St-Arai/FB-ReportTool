package FindBugsManager.UI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.commons.lang.math.NumberUtils;

import FindBugsManager.Core.Execute;
import FindBugsManager.Core.Main;
import FindBugsManager.Core.Settings;
import FindBugsManager.Core.XMLManager;
import FindBugsManager.DataSets.BugData;
import FindBugsManager.DataSets.BugInstanceSet;
import FindBugsManager.DataSets.PersonalData;
import FindBugsManager.FindBugs.FindBugsManager;
import FindBugsManager.Git.AccountManager;
import FindBugsManager.Git.CheckoutManager;
import FindBugsManager.Git.CommitInfo;
import FindBugsManager.Git.CommitManager;

public class GitScanning implements ActionListener {

	private ArrayList<CommitInfo> _commitLog = new ArrayList<CommitInfo>();
	private ArrayList<CommitInfo> _parentLog = new ArrayList<CommitInfo>();

	private File _file = Main.getGitFile();

	private CommitManager commit = new CommitManager(_file);
	private AccountManager account = AccountManager.getInstance();
	private CheckoutManager check = new CheckoutManager();
	private Execute execute = Execute.getInctance();

	private JFrame _frame = null;
	private JPanel panel = new JPanel();

	private JCheckBox chboxDouble = new JCheckBox("×2");
	private JCheckBox chboxCateg = new JCheckBox("Category Bonus ×3");
	private JCheckBox chboxAuto = new JCheckBox("Auto Pull");

	private JComboBox<String> _targetBranches = new JComboBox<String>();
	private JComboBox<String> _parentBranches = new JComboBox<String>();
	private JComboBox<String> _committerList = new JComboBox<String>();

	private String[] comboData = {"CORRECTNESS", "SECURITY", "BAD_PRACTICE", "STYLE",
			"PERFORMANCE", "MALICIOUS_CODE", "MT_CORRECTNESS", "I18N"};
	private JComboBox<String> categoryList = new JComboBox<String>(comboData);

	private String bugDataPath = Settings.getBugDataStorePath();
	private final File bugDataDirectory = new File(bugDataPath);

	private String targetPath = Main.getTargetPath();

	public GitScanning(JFrame mainFrame) {
		_frame = mainFrame;

		initCommitInfo();
		initCommitterInfo();

		JButton updatebutton = new JButton("Update");
		updatebutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				check.backtoLatestRevision();
				_frame.remove(panel);
				new GitScanning(_frame);
			}
		});
		JButton button1 = new JButton("Run FindBugs");
		JButton button2 = new JButton("Make BugInfo File");
		JButton button3 = new JButton("Show Personal Result");
		JButton button4 = new JButton("Show All Result");
		JButton button5 = new JButton("Read all");

		button1.setActionCommand("1");
		button2.setActionCommand("2");
		button3.setActionCommand("3");
		button4.setActionCommand("4");
		button5.setActionCommand("5");
		button1.addActionListener(this);
		button2.addActionListener(this);
		button3.addActionListener(this);
		button4.addActionListener(this);
		button5.addActionListener(this);

		_targetBranches.addActionListener(this);
		_targetBranches.setActionCommand("TargetChanged");
		_parentBranches.addActionListener(this);
		_parentBranches.setActionCommand("ParentChanged");
		_committerList.addActionListener(this);
		_committerList.setActionCommand("CommitterChanged");

		categoryList.addActionListener(this);
		categoryList.setActionCommand("CategoryChanged");

		panel.add(_targetBranches);
		panel.add(updatebutton);
		panel.add(button1);
		panel.add(button2);
		panel.add(_parentBranches);
		panel.add(_committerList);
		panel.add(button3);
		panel.add(button4);
		panel.add(chboxDouble);
		panel.add(chboxCateg);
		panel.add(categoryList);
		panel.add(button5);

		chboxAuto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chboxAuto.isSelected()) {
					execute.startPullThread();
				} else {
					execute.stopPullThread();
				}
			}
		});
		panel.add(chboxAuto);

		_frame.add(panel, BorderLayout.CENTER);

		_frame.setVisible(true);

	}

	private void initCommitInfo() {
		commit.setCommitLogs();
		String[] info = commit.getAllCommitList();
		commit.initBugFileList();

		_commitLog = commit.getCommitLog();
		_targetBranches = new JComboBox<String>(info);
	}

	private void initCommitterInfo() {
		_committerList.removeAllItems();
		ArrayList<PersonalData> dataList = account.getPersonalDataList();
		for (PersonalData data : dataList) {
			_committerList.addItem(data.getName());
		}
		_committerList.repaint();
	}

	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		execute.pauseAutoPull();

		int targetIndex = _targetBranches.getSelectedIndex();
		int parentIndex = _parentBranches.getSelectedIndex();

		int committerNum = _committerList.getSelectedIndex();
		int categIndex = categoryList.getSelectedIndex();

		boolean isNumber = NumberUtils.isNumber(action);
		if (isNumber) {
			if (parentIndex < 0) {
				return;
			}
			int commandNum = Integer.parseInt(action);

			CommitInfo targetCommitInfo = _commitLog.get(targetIndex);
			String targetCommit = targetCommitInfo.getCommitName();
			String targetComment = targetCommitInfo.getCommitMessage().replaceAll("\n", "");
			int commitNumber = targetCommitInfo.getCommitNumber();
			targetComment = setValidCommitName(targetComment, commitNumber);

			CommitInfo parentCommitInfo = _parentLog.get(parentIndex);
			String parentCommit = parentCommitInfo.getCommitName();
			String parentComment = parentCommitInfo.getCommitMessage().replaceAll("\n", "");
			int parentNumber = parentCommitInfo.getCommitNumber();
			parentComment = setValidCommitName(parentComment, parentNumber);

			switch (commandNum) {
				case 1 :
					check.backtoLatestRevision();
					File[] files = bugDataDirectory.listFiles();
					boolean preExist = false;
					boolean curExist = false;
					for (File file : files) {
						if ((parentComment + ".xml").equals(file.getName())) {
							preExist = true;
						} else if ((targetComment + ".xml").equals(file.getName())) {
							curExist = true;
						}
					}
					if (curExist == false) {
						int running = checkoutAndRun(targetCommit, targetComment);
						if (running != 0) {
							break;
						}
					}
					if (preExist == false) {
						checkoutAndRun(parentCommit, parentComment);
					}
					break;
				case 2 :
					outputBugsResult(targetIndex, parentIndex, categIndex);
					initCommitterInfo();
					check.backtoLatestRevision();
					break;
				case 3 :
					if (committerNum >= 0) {
						String target = _committerList.getItemAt(committerNum);
						new PersonalDisplay(new JFrame(), target);
					}
					break;
				case 4 :
					new AllResultsDisplay(new JFrame());
					break;
				case 5 :

				default :
					break;
			}
		} else if (action.equals("TargetChanged")) {
			_parentBranches.removeAllItems();
			CommitInfo targetCommitInfo = _commitLog.get(targetIndex);
			ArrayList<CommitInfo> parents = targetCommitInfo.getParentCommits();
			if (parents.size() > 1) {
				for (CommitInfo parent : parents) {
					_parentBranches.addItem(commit.getCommitList(parent));
				}
			} else {
				_parentBranches.addItem(commit.getCommitList(parents.get(0)));
			}
			_parentBranches.repaint();

			_parentLog = commit.getParentLog();
		}
		execute.resumeAutoPull();
	}

	private void outputBugsResult(int targetIndex, int parentIndex, int categIndex) {

		FindBugsManager manager = FindBugsManager.getInstance();
		CommitInfo targetCommitInfo = _commitLog.get(targetIndex);
		CommitInfo parentCommitInfo = _parentLog.get(parentIndex);
		String committer = targetCommitInfo.getCommitter();
		manager.setCommitter(committer);

		String targetCommit = targetCommitInfo.getCommitName();
		String targetComment = targetCommitInfo.getCommitMessage().replaceAll("\n", "");
		int commitNumber = targetCommitInfo.getCommitNumber();
		targetComment = setValidCommitName(targetComment, commitNumber);

		int miss = 0;
		File currentOutput = new File(bugDataDirectory, targetComment + ".xml");
		if (!(currentOutput.exists())) {
			System.out.println("Run FindBugs...");
			miss = checkoutAndRun(targetCommit, targetComment);
		}
		System.out.println("Now");
		manager.createBugInfoList(currentOutput);

		String parentCommit = parentCommitInfo.getCommitName();
		String parentComment = parentCommitInfo.getCommitMessage().replaceAll("\n", "");
		int parentNumber = parentCommitInfo.getCommitNumber();
		parentComment = setValidCommitName(parentComment, parentNumber);

		File parentOutput = new File(bugDataDirectory, parentComment + ".xml");
		if (!(parentOutput.exists())) {
			System.out.println("Run FindBugs...");
			checkoutAndRun(parentCommit, parentComment);
		}
		System.out.println("Past");
		manager.createPreBugInfoList(parentOutput);

		int bonus = 1;
		String category = null;
		int categBonus = 1;
		if (chboxDouble.isSelected()) {
			bonus *= 2;
		}
		if (chboxCateg.isSelected()) {
			category = categoryList.getItemAt(categIndex);
			categBonus *= 3;
		}
		execute.checkFixerName();

		ArrayList<BugInstanceSet> edited = manager.getEditedBugList();
		ArrayList<BugData> data = new ArrayList<BugData>();
		for (BugInstanceSet set : edited) {
			if (set.getBugInstance().getBugPattern().getCategory().equals(category)) {
				data.add(new BugData(set, bonus * categBonus));
			} else {
				data.add(new BugData(set, bonus));
			}
		}
		account.updatePersonalData(committer, data, miss);

		account.addPersonalData(new PersonalData(committer));

		String comId = targetCommitInfo.getCommitName().substring(0, 4);
		String preComId = parentCommitInfo.getCommitName().substring(0, 4);
		String id = setOutputFileName(commitNumber, parentNumber, comId, preComId);

		XMLManager xml = new XMLManager();
		xml.createXML(manager, id, bonus, category, categBonus);
		manager.initBugInfoLists();

	}

	private int checkoutAndRun(String commitName, String commitComment) {
		CheckoutManager check = new CheckoutManager();
		check.checkoutCommand(commitName);
		int running = FindBugsManager.runFindbugs(commitComment, targetPath, bugDataDirectory);
		check.backtoLatestRevision();
		return running;
	}

	private String setValidCommitName(String selectedComment, int index) {
		String strNumber = String.valueOf(index);
		StringBuilder stb = new StringBuilder();
		stb.append(strNumber);
		stb.append(selectedComment);
		String comment = stb.toString();
		int place = comment.indexOf(":");
		if (place > 0) {
			comment = comment.substring(0, place).replaceAll("'", "").replaceAll("/", "")
					.replaceAll(".com", "");
		}
		return comment;
	}

	private String setOutputFileName(int targetNumber, int parentNumber, String comId,
			String preComId) {
		StringBuilder stb = new StringBuilder();
		stb.append(String.valueOf(parentNumber));
		stb.append("to");
		stb.append(String.valueOf(targetNumber));
		stb.append("_");
		stb.append(comId);
		stb.append(preComId);
		String id = stb.toString();
		return id;
	}
}