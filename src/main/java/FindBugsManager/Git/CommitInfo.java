package FindBugsManager.Git;

import java.sql.Date;
import java.util.ArrayList;

import org.eclipse.jgit.revwalk.RevCommit;

public class CommitInfo {

	private String _commit = null;
	private String _committer = null;
	private int _time = 0;
	private String _date = null;
	private String _message = null;

	private ArrayList<CommitInfo> _parents = null;

	public CommitInfo(RevCommit revCommit) {
		setCommitInfo(revCommit);
	}

	public CommitInfo(RevCommit revCommit, RevCommit[] parents) {
		setCommitInfo(revCommit);

		_parents = new ArrayList<CommitInfo>();
		for (RevCommit commit : parents) {
			_parents.add(new CommitInfo(commit));
		}
	}

	private void setCommitInfo(RevCommit revCommit) {
		_commit = revCommit.getName();
		_committer = revCommit.getAuthorIdent().getName();
		_time = revCommit.getCommitTime();
		_message = revCommit.getFullMessage();

		long longtime = (long) _time * 1000;
		_date = new Date(longtime).toString();
	}

	public String getCommitName() {
		return _commit;
	}

	public String getCommitter() {
		return _committer;
	}

	public int getCommitTime() {
		return _time;
	}

	public String getCommitDate() {
		return _date;
	}

	public String getCommitMessage() {
		return _message;
	}

	public ArrayList<CommitInfo> getParentCommits() {
		return _parents;
	}
}
