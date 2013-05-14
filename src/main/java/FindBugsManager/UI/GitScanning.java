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
import FindBugsManager.Core.XMLManager;
import FindBugsManager.FindBugs.CommitInfo;
import FindBugsManager.FindBugs.CommitManager;
import FindBugsManager.FindBugs.FindBugsManager;

public class GitScanning implements ActionListener {

	private ArrayList<CommitInfo> commitLog = new ArrayList<CommitInfo>();

	private File _file = Main.getGitFile();
	private String _path = Main.getFilePath();
	private CommitManager commit = new CommitManager(_file, _path);;

	private JPanel panel = new JPanel();

	private JComboBox<String> checkoutBranches = new JComboBox<String>();
	private JComboBox<String> targetBugFile = new JComboBox<String>();

	private transient Repository repos = null;

	private static final File bugsRepository = new File("../bugOutput/FindBugsFiles");
	private static final String targetPath = "D:/Users/ALEXANDRITE/Projects/FBsample/bin/src/FBsample.class";
	// private static final String targetPath =
	// "C:/Projects/workspace/FBsample/bin/src/FBsample.class";

	private int bugsIndex = 0;

	public GitScanning(JFrame frame) {

		commit.initCommitLogs();
		commit.initBugFileList();

		this.commitLog = commit.getCommitLog();
		this.checkoutBranches = commit.getBranchList();
		this.targetBugFile = commit.getTargetBugFileList();

		JButton button3 = new JButton("Checkout");
		JButton button4 = new JButton("Up to date");
		JButton button5 = new JButton("Run FindBugs");
		JButton button6 = new JButton("Make BugInfo File");
		button3.setActionCommand("3");
		button4.setActionCommand("4");
		button5.setActionCommand("5");
		button6.setActionCommand("6");
		button3.addActionListener(this);
		button4.addActionListener(this);
		button5.addActionListener(this);
		button6.addActionListener(this);
		checkoutBranches.addActionListener(this);
		targetBugFile.addActionListener(this);

		panel.add(checkoutBranches);
		panel.add(button3);
		panel.add(button4);
		panel.add(button5);
		panel.add(button6);
		panel.add(targetBugFile);

		frame.add(panel, BorderLayout.CENTER);

		frame.setVisible(true);

	}

	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if (!(action.equals("comboBoxChanged"))) {
			int commandNum = Integer.parseInt(e.getActionCommand());
			int index = checkoutBranches.getSelectedIndex();
			String selectedCommit = commitLog.get(index).getCommitName();
			String selectedComment = commitLog.get(index).getCommitMessage().replaceAll("\n", "");

			bugsIndex = targetBugFile.getSelectedIndex();

			switch (commandNum) {
				case 3 :
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
					System.out.println(selectedComment);
					System.out.println(selectedCommit + "\n");

					break;
				case 4 :
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

					break;
				case 5 :
					Runtime rt = Runtime.getRuntime();
					try {
						rt.exec(new String[]{"cmd.exe", "/C", "findbugs", "-textui", "-low",
								"-xml", "-output", selectedComment + ".xml", targetPath}, null,
								bugsRepository);
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					break;
				case 6 :
					FindBugsManager manager = FindBugsManager.getInstance();
					XMLManager xml = new XMLManager();

					String current = commitLog.get(0).getCommitMessage().replaceAll("\n", "");
					File currentOutput = new File(bugsRepository, current + ".xml");

					if (currentOutput.exists()) {
						manager.createBugInfoList(currentOutput);
					} else {
						//
					}

					String target = targetBugFile.getItemAt(bugsIndex);
					File targetOutput = new File(bugsRepository, target + ".xml");

					if (targetOutput.exists()) {
						manager.createPreBugInfoList(targetOutput);
					} else {
						//
					}

					Execute execute = new Execute(_file, _path);
					execute.check();

					xml.createXML(manager);

					break;
				default :
					break;
			}
		}
	}
}