package FindBugsManager.FindBugs;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepository;

import FindBugsManager.Core.Execute;
import FindBugsManager.Core.XMLManager;

public class CommitManager extends GitManager implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<CommitInfo> commitLog = new ArrayList<CommitInfo>();

	private JFrame frame = new JFrame();
	private JPanel panel = new JPanel();
	private JComboBox<String> checkoutBranches = new JComboBox<String>();
	private JComboBox<String> targetBugFile = new JComboBox<String>();

	private Repository repos = null;

	private final File cmp = new File("../bugOutput/Comparisons");
	private final File bugs = new File("../bugOutput/FindBugsFiles");

	public CommitManager(File file, String path) {
		super(file, path);

		findCommiter();
	}

	private void findCommiter() {
		try {
			repos = new FileRepository(_file);
			Git git = new Git(repos);

			LogCommand log = git.log();
			for (RevCommit revCommit : log.call()) {
				String commit = revCommit.getName();
				String author = revCommit.getAuthorIdent().getName();
				int time = revCommit.getCommitTime();
				String message = revCommit.getFullMessage();
				commitLog.add(new CommitInfo(commit, author, time, message));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoHeadException e) {
			e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<CommitInfo> getCommitLog() {
		return commitLog;
	}

	private void initCommitLogs() {

		String[] info = new String[commitLog.size()];
		int i = 0;
		for (CommitInfo commit : commitLog) {
			System.out.println(commit.getCommitName());
			System.out.println(commit.getAuthor());
			System.out.println(commit.getCommitTime());
			System.out.println(commit.getCommitMessage());
			info[i] = "<html>" + commit.getCommitTime() + "    :    " + commit.getAuthor()
					+ "<br/>" + commit.getCommitMessage() + "</html>";
			i++;
		}
		checkoutBranches = new JComboBox<String>(info);
	}

	private void initBugFileList() {
		File[] bugFiles = bugs.listFiles();
		String[] info = new String[bugFiles.length];
		int i = 0;

		for (File file : bugFiles) {
			info[i] = file.getName();
			i++;
		}
		targetBugFile = new JComboBox<String>(info);
	}

	@Override
	public void display() {
		frame.setSize(new Dimension(1000, 750));
		frame.setTitle("");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		initCommitLogs();
		initBugFileList();

		JButton button1 = new JButton("Log in");
		JButton button2 = new JButton("Cancel");
		JButton button3 = new JButton("Checkout");
		JButton button4 = new JButton("Up to date");
		JButton button5 = new JButton("Run FindBugs");
		JButton button6 = new JButton("Make BugInfo File");
		button1.setActionCommand("1");
		button2.setActionCommand("2");
		button3.setActionCommand("3");
		button4.setActionCommand("4");
		button5.setActionCommand("5");
		button6.setActionCommand("6");
		button1.addActionListener(this);
		button2.addActionListener(this);
		button3.addActionListener(this);
		button4.addActionListener(this);
		button5.addActionListener(this);
		button6.addActionListener(this);
		checkoutBranches.addActionListener(this);
		targetBugFile.addActionListener(this);

		panel.add(button1);
		panel.add(button2);
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

			int bugsIndex = targetBugFile.getSelectedIndex();

			switch (commandNum) {
				case 1 :
					break;
				case 2 :
					break;
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
								"-xml", "-output", selectedComment + ".xml",
								"D:/Users/ALEXANDRITE/Projects/FBsample/bin/src/FBsample.class"},
								null, bugs);
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					break;
				case 6 :
					FindBugsManager manager = FindBugsManager.getInstance();
					XMLManager xml = new XMLManager();

					String current = commitLog.get(0).getCommitMessage().replaceAll("\n", "");
					File currentOutput = new File(bugs, current + ".xml");

					if (currentOutput.exists()) {
						System.out.println(current);
						manager.createBugInfoList(currentOutput);
					} else {
						//
						System.out.println(current);
					}

					// String target = targetBugFile.getItemAt(bugsIndex);
					// File targetOutput = new File(bugs, target);
					//
					// if (targetOutput.exists()) {
					// System.out.println(target);
					// manager.createBugInfoList(targetOutput);
					// } else {
					// //
					// System.out.println(target);
					// }

					String previous = commitLog.get(1).getCommitMessage().replaceAll("\n", "");

					File preOutput = new File(previous + ".xml");
					if (preOutput.exists()) {
						System.out.println("EXISTS");
						manager.createPreBugInfoList(preOutput);
					}
					Execute execute = new Execute(_file, _path);
					execute.check();

					xml.createXML(manager);
				default :
					break;
			}
		}
	}
}
