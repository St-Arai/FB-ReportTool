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

	private File _file = Main.getGitFile();
	private String _path = Main.getFilePath();

	private CommitManager commit = new CommitManager(_file);
	private AccountManager account = AccountManager.getInstance();

	private JFrame _frame = null;
	private JPanel panel = new JPanel();

	private JCheckBox chboxdouble = new JCheckBox("×2");
	private JCheckBox chboxcateg = new JCheckBox("Category Bonus ×3");

	private JComboBox<String> _checkoutBranches = new JComboBox<String>();
	private JComboBox<String> _targetBranches = new JComboBox<String>();
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
		JButton button1 = new JButton("Run FindBugs");
		JButton button2 = new JButton("Make BugInfo File");
		JButton button3 = new JButton("Show Personal Result");
		JButton button4 = new JButton("Show All Result");
		updatebutton.setActionCommand("0");
		button1.setActionCommand("1");
		button2.setActionCommand("2");
		button3.setActionCommand("3");
		button4.setActionCommand("4");
		updatebutton.addActionListener(this);
		button1.addActionListener(this);
		button2.addActionListener(this);
		button3.addActionListener(this);
		button4.addActionListener(this);
		_checkoutBranches.addActionListener(this);
		_targetBranches.addActionListener(this);
		_committerList.addActionListener(this);
		categoryList.addActionListener(this);

		panel.add(_checkoutBranches);
		panel.add(updatebutton);
		panel.add(button1);
		panel.add(button2);
		panel.add(_targetBranches);
		panel.add(_committerList);
		panel.add(button3);
		panel.add(button4);
		panel.add(chboxdouble);
		panel.add(chboxcateg);
		panel.add(categoryList);

		_frame.add(panel, BorderLayout.CENTER);

		_frame.setVisible(true);

	}

	private void initCommitInfo() {
		commit.setCommitLogs();
		String[] info = commit.getCommitList();
		commit.initBugFileList();

		_commitLog = commit.getCommitLog();
		_checkoutBranches = new JComboBox<String>(info);
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
		if (!(action.equals("comboBoxChanged"))) {
			int commandNum = Integer.parseInt(action);
			int selectedIndex = _checkoutBranches.getSelectedIndex();
			int targetIndex = _targetBranches.getSelectedIndex();

			int committerNum = _committerList.getSelectedIndex();
			int categIndex = categoryList.getSelectedIndex();

			CommitInfo selectedCommitInfo = _commitLog.get(selectedIndex);
			String selectedCommit = selectedCommitInfo.getCommitName();
			String selectedComment = selectedCommitInfo.getCommitMessage().replaceAll("\n", "");
			selectedComment = setValidCommitName(selectedComment, selectedIndex);

			CommitInfo targetCommitInfo = _commitLog.get(targetIndex);
			String targetCommit = targetCommitInfo.getCommitName();
			String targetComment = targetCommitInfo.getCommitMessage().replaceAll("\n", "");
			targetComment = setValidCommitName(targetComment, targetIndex);

			CheckoutManager check = new CheckoutManager();
			switch (commandNum) {
				case 0 :
					check.backtoLatestRevision();
					_frame.remove(panel);
					new GitScanning(_frame);
					break;
				case 1 :
					File[] files = bugDataDirectory.listFiles();
					boolean preExist = false;
					boolean curExist = false;
					for (File file : files) {
						if ((targetComment + ".xml").equals(file.getName())) {
							preExist = true;
						} else if ((selectedComment + ".xml").equals(file.getName())) {
							curExist = true;
						}
					}
					if (preExist == false) {
						checkoutAndRun(targetCommit, targetComment);
					}
					if (curExist == false) {
						checkoutAndRun(selectedCommit, selectedComment);
					} else {
						check.backtoLatestRevision();
					}
					break;
				case 2 :
					outputBugsResult(selectedIndex, targetIndex, categIndex);
					initCommitterInfo();
					check.backtoLatestRevision();
					break;
				case 3 :
					String target = _committerList.getItemAt(committerNum);
					new PersonalDisplay(new JFrame(), target);
					break;
				case 4 :
					new AllResultsDisplay(new JFrame());
					break;
				default :
					break;
			}
		} else if (action.equals("comboBoxChanged")) {
		} else {
			System.out.println(action);
		}
	}

	private void outputBugsResult(int selectedIndex, int targetIndex, int categIndex) {
		FindBugsManager manager = FindBugsManager.getInstance();
		CommitInfo selectedCommitInfo = _commitLog.get(selectedIndex);
		CommitInfo targetCommitInfo = _commitLog.get(targetIndex);
		String committer = selectedCommitInfo.getCommitter();
		manager.setCommitter(committer);

		String selectedCommit = selectedCommitInfo.getCommitName();
		String selectedComment = selectedCommitInfo.getCommitMessage().replaceAll("\n", "");
		selectedComment = setValidCommitName(selectedComment, selectedIndex);
		int miss = 0;
		File currentOutput = new File(bugDataDirectory, selectedComment + ".xml");
		if (!(currentOutput.exists())) {
			System.out.println("Run FindBugs...");
			miss = checkoutAndRun(selectedCommit, selectedComment);
		}
		System.out.println("Now");
		manager.createBugInfoList(currentOutput);

		String targetCommit = targetCommitInfo.getCommitName();
		String targetComment = targetCommitInfo.getCommitMessage().replaceAll("\n", "");
		targetComment = setValidCommitName(targetComment, targetIndex);
		File targetOutput = new File(bugDataDirectory, targetComment + ".xml");
		if (!(targetOutput.exists())) {
			System.out.println("Run FindBugs...");
			checkoutAndRun(targetCommit, targetComment);
		}
		System.out.println("Past");
		manager.createPreBugInfoList(targetOutput);

		int bonus = 1;
		String category = null;
		int categBonus = 1;
		if (chboxdouble.isSelected()) {
			bonus *= 2;
		}
		if (chboxcateg.isSelected()) {
			category = categoryList.getItemAt(categIndex);
			categBonus *= 3;
		}

		Execute execute = new Execute(_file, _path);
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

		String comId = selectedCommitInfo.getCommitName().substring(0, 4);
		String preComId = targetCommitInfo.getCommitName().substring(0, 4);
		String id = setOutputFileName(selectedIndex, targetIndex, comId, preComId);

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
		String strNumber = String.valueOf(getRevisionNumber(index));
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

	private String setOutputFileName(int selectedIndex, int targetIndex, String comId,
			String preComId) {
		StringBuilder stb = new StringBuilder();
		stb.append(String.valueOf(getRevisionNumber(targetIndex)));
		stb.append("to");
		stb.append(String.valueOf(getRevisionNumber(selectedIndex)));
		stb.append("_");
		stb.append(comId);
		stb.append(preComId);
		String id = stb.toString();
		return id;
	}

	private int getRevisionNumber(int index) {
		int length = commit.getCommitLength();
		int revisionNumber = length - index;
		return revisionNumber;
	}
}