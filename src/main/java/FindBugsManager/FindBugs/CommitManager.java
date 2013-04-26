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

public class CommitManager extends GitManager implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<CommitInfo> commitLog = new ArrayList<CommitInfo>();

	private JFrame frame = new JFrame();
	private JComboBox<String> combo;

	private Repository repos = null;

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoHeadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<CommitInfo> getCommitLog() {
		return commitLog;
	}

	@Override
	public void display() {
		frame.setSize(new Dimension(1000, 750));
		frame.setTitle("");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// String[] combodata = {"Swing", "Java2D", "Java3D", "JavaMail"};
		String[] info = new String[commitLog.size()];
		int i = 0;
		for (CommitInfo commit : commitLog) {
			System.out.println(commit.getCommitName());
			System.out.println(commit.getAuthor());
			System.out.println(commit.getCommitTime());
			System.out.println(commit.getCommitMessage());
			info[i] = "<html>" + commit.getCommitTime() + "    :    "
					+ commit.getAuthor() + "<br/>" + commit.getCommitMessage()
					+ "</html>";
			i++;
		}
		combo = new JComboBox<String>(info);

		JPanel panel = new JPanel();

		JButton button = new JButton("Log in");
		JButton button2 = new JButton("Cancel");
		JButton button3 = new JButton("Checkout");
		button.setActionCommand("1");
		button2.setActionCommand("2");
		button3.setActionCommand("3");
		button.addActionListener(this);
		button2.addActionListener(this);
		button3.addActionListener(this);
		combo.addActionListener(this);

		panel.add(button);
		panel.add(button2);
		panel.add(combo);
		panel.add(button3);

		frame.add(panel, BorderLayout.CENTER);

		frame.setVisible(true);

	}

	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if (!(action.equals("comboBoxChanged"))) {
			int commandNum = Integer.parseInt(e.getActionCommand());
			int index = 0;
			switch (commandNum) {
				case 1 :
					// frame.getContentPane().removeAll();
					break;
				case 2 :
					// frame.getContentPane().removeAll();
					break;
				case 3 :
					// frame.getContentPane().removeAll();
					index = combo.getSelectedIndex();
					String selectedCommit = commitLog.get(index)
							.getCommitName();
					try {
						repos = new FileRepository(_file);
						Git git = new Git(repos);
						CheckoutCommand check = git.checkout();
						check.setName(selectedCommit);
						check.call();

					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (RefAlreadyExistsException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (RefNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (InvalidRefNameException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (CheckoutConflictException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (GitAPIException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					System.out.println(commitLog.get(index).getCommitMessage()
							.replaceAll("\n", ""));
					System.out.println(selectedCommit + "\n");
					break;
				default :
					break;
			}
		}
		// frame.repaint();
	}
}
