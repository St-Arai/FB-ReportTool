package FindBugsManager.UI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepository;

import FindBugsManager.Core.Execute;
import FindBugsManager.Core.Main;
import FindBugsManager.Core.Settings;
import FindBugsManager.Core.XMLManager;
import FindBugsManager.FindBugs.CommitInfo;
import FindBugsManager.FindBugs.CommitManager;
import FindBugsManager.FindBugs.FindBugsManager;

public class GitScanning implements ActionListener {

	private ArrayList<CommitInfo> _commitLog = new ArrayList<CommitInfo>();

	private File _file = Main.getGitFile();
	private String _path = Main.getFilePath();

	private CommitManager commit = new CommitManager(_file);;

	private JFrame _frame = null;
	private JPanel panel = new JPanel();

	private JComboBox<String> _checkoutBranches = new JComboBox<String>();
	private JComboBox<String> _targetBranches = new JComboBox<String>();

	private transient Repository repos = null;

	private String bugDataPath = Settings.getBugDataStorePath();
	private final File bugDataDirectory = new File(bugDataPath);

	// private static final String targetPath =
	// "D:/Users/ALEXANDRITE/Projects/FBsample/bin/src/FBsample.class";
	private static final String targetPath = "../FBsample/bin/src/FBsample.class";

	public GitScanning(JFrame mainFrame) {
		_frame = mainFrame;

		initCommitInfo();

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

		panel.add(_checkoutBranches);
		panel.add(button1);
		panel.add(button2);
		panel.add(_targetBranches);
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

	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if (!(action.equals("comboBoxChanged"))) {
			int commandNum = Integer.parseInt(action);
			int index = _checkoutBranches.getSelectedIndex();
			String selectedCommit = _commitLog.get(index).getCommitName();
			String selectedComment = _commitLog.get(index).getCommitMessage().replaceAll("\n", "");

			String previousCommit = _commitLog.get(index + 1).getCommitName();
			String previousComment = _commitLog.get(index + 1).getCommitMessage()
					.replaceAll("\n", "");

			switch (commandNum) {
				case 1 :
					checkoutCommand(previousCommit);
					runFindbugs(previousComment);

					checkoutCommand(selectedCommit);
					runFindbugs(selectedComment);
					break;
				case 2 :
					outputBugsResult(index);
					backtoLatestRevision();
					break;
				case 3 :
					new PersonalDisplay(new JFrame());
					break;
				default :
					break;
			}
		} else if (action.equals("comboBoxChanged")) {
			_targetBranches.removeAllItems();
			int index = _checkoutBranches.getSelectedIndex();
			String item = _checkoutBranches.getItemAt(index + 1);
			_targetBranches.addItem(item);
			_frame.repaint();
		} else {
			System.out.println(action);
		}
	}

	private void outputBugsResult(int index) {
		FindBugsManager manager = FindBugsManager.getInstance();
		XMLManager xml = new XMLManager();

		String current = _commitLog.get(index).getCommitMessage().replaceAll("\n", "");
		File currentOutput = new File(bugDataDirectory, current + ".xml");

		if (!(currentOutput.exists())) {
			runFindbugs(current);
		}
		manager.createBugInfoList(currentOutput);

		String target = _commitLog.get(index + 1).getCommitMessage().replaceAll("\n", "");;
		File targetOutput = new File(bugDataDirectory, target + ".xml");

		if (!(targetOutput.exists())) {
			System.out.println("Not Found a previous file...");
			return;
		}
		manager.createPreBugInfoList(targetOutput);

		Execute execute = new Execute(_file, _path);
		execute.checkFixerName();

		xml.createXML(manager);
	}

	private void checkoutCommand(String selectedCommit) {
		try {
			repos = new FileRepository(_file);
			Git git = new Git(repos);
			CheckoutCommand check = git.checkout();
			check.setName(selectedCommit);
			check.call();

		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (RefAlreadyExistsException e1) {
			e1.printStackTrace();
		} catch (RefNotFoundException e1) {
			e1.printStackTrace();
		} catch (InvalidRefNameException e1) {
			e1.printStackTrace();
		} catch (CheckoutConflictException e1) {
			e1.printStackTrace();
		} catch (GitAPIException e1) {
			e1.printStackTrace();
		}
	}

	private void runFindbugs(String selectedComment) {
		Runtime rt = Runtime.getRuntime();
		Process p = null;
		try {
			p = rt.exec(new String[]{"cmd.exe", "/C", "findbugs", "-textui", "-low", "-xml",
					"-output", selectedComment + ".xml", targetPath}, null, bugDataDirectory);
			p.waitFor();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void backtoLatestRevision() {
		try {
			repos = new FileRepository(_file);
			Git git = new Git(repos);
			CheckoutCommand check = git.checkout();
			check.setName("master");
			check.call();

		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (RefAlreadyExistsException e1) {
			e1.printStackTrace();
		} catch (RefNotFoundException e1) {
			e1.printStackTrace();
		} catch (InvalidRefNameException e1) {
			e1.printStackTrace();
		} catch (CheckoutConflictException e1) {
			e1.printStackTrace();
		} catch (GitAPIException e1) {
			e1.printStackTrace();
		}
	}
}