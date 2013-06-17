package FindBugsManager.UI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import FindBugsManager.Core.Execute;
import FindBugsManager.Core.Main;
import FindBugsManager.Core.Settings;
import FindBugsManager.Core.XMLManager;
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

	private JComboBox<String> _checkoutBranches = new JComboBox<String>();
	private JComboBox<String> _targetBranches = new JComboBox<String>();
	private JComboBox<String> _committerList = new JComboBox<String>();

	private String bugDataPath = Settings.getBugDataStorePath();
	private final File bugDataDirectory = new File(bugDataPath);

	private static final String targetPath = "D:/Users/ALEXANDRITE/Projects/FBsample/bin/src/FBsample.class";
	// private static final String targetPath = "../FBsample/bin/src/FBsample.class";

	public GitScanning(JFrame mainFrame) {
		_frame = mainFrame;

		initCommitInfo();
		initCommitterInfo();

		JButton button1 = new JButton("Run FindBugs");
		JButton button2 = new JButton("Make BugInfo File");
		JButton button3 = new JButton("Show Result");
		button1.setActionCommand("1");
		button2.setActionCommand("2");
		button3.setActionCommand("3");
		button1.addActionListener(this);
		button2.addActionListener(this);
		button3.addActionListener(this);
		_checkoutBranches.addActionListener(this);
		_targetBranches.addActionListener(this);
		_committerList.addActionListener(this);

		panel.add(_checkoutBranches);
		panel.add(button1);
		panel.add(button2);
		panel.add(_targetBranches);
		panel.add(_committerList);
		panel.add(button3);

		_frame.add(panel, BorderLayout.CENTER);

		_frame.setVisible(true);

	}

	private void initCommitInfo() {
		commit.findCommiter();
		commit.setCommitLogs();
		commit.initBugFileList();

		_commitLog = commit.getCommitLog();
		_checkoutBranches = commit.getBranchList();
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
			int index = _checkoutBranches.getSelectedIndex();
			int committerNum = _committerList.getSelectedIndex();
			String selectedCommit = _commitLog.get(index).getCommitName();
			String selectedComment = _commitLog.get(index).getCommitMessage().replaceAll("\n", "");

			String previousCommit = _commitLog.get(index + 1).getCommitName();
			String previousComment = _commitLog.get(index + 1).getCommitMessage()
					.replaceAll("\n", "");

			CheckoutManager check = new CheckoutManager();
			switch (commandNum) {
				case 1 :
					check.checkoutCommand(previousCommit);
					FindBugsManager.runFindbugs(previousComment, targetPath, bugDataDirectory);

					check.checkoutCommand(selectedCommit);
					FindBugsManager.runFindbugs(selectedComment, targetPath, bugDataDirectory);
					break;
				case 2 :
					outputBugsResult(index);
					initCommitterInfo();
					check.backtoLatestRevision();
					break;
				case 3 :
					String target = _committerList.getItemAt(committerNum);
					new PersonalDisplay(new JFrame(), target);
					break;
				default :
					break;
			}
		} else if (action.equals("comboBoxChanged")) {
			_targetBranches.removeAllItems();
			int index = _checkoutBranches.getSelectedIndex();
			String item = _checkoutBranches.getItemAt(index + 1);
			_targetBranches.addItem(item);
			_targetBranches.repaint();
		} else {
			System.out.println(action);
		}
	}

	private void outputBugsResult(int index) {
		FindBugsManager manager = FindBugsManager.getInstance();
		String committer = _commitLog.get(index).getCommitter();
		manager.setCommitter(committer);

		XMLManager xml = new XMLManager();
		String comId = _commitLog.get(index).getCommitName().substring(0, 4);
		String preComId = _commitLog.get(index + 1).getCommitName().substring(0, 4);
		String id = String.valueOf(index + 1) + "to" + String.valueOf(index) + "_" + comId
				+ preComId;

		String current = _commitLog.get(index).getCommitMessage().replaceAll("\n", "");
		File currentOutput = new File(bugDataDirectory, current + ".xml");

		if (!(currentOutput.exists())) {
			FindBugsManager.runFindbugs(current, targetPath, bugDataDirectory);
		}
		manager.createBugInfoList(currentOutput);

		String target = _commitLog.get(index + 1).getCommitMessage().replaceAll("\n", "");
		File targetOutput = new File(bugDataDirectory, target + ".xml");

		if (!(targetOutput.exists())) {
			System.out.println("Not Found a previous file...");
			return;
		}
		manager.createPreBugInfoList(targetOutput);

		Execute execute = new Execute(_file, _path);
		execute.checkFixerName();

		ArrayList<BugInstanceSet> edited = manager.getEditedBugList();
		int sum = 0;
		for (BugInstanceSet info : edited) {
			sum += (21 - info.getBugInstance().getBugRank());
		}
		account.updatePersonalData(committer, sum, edited);

		xml.createXML(manager, id);
		manager.initBugInfoLists();
	}
}