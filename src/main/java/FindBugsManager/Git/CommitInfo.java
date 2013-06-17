package FindBugsManager.Git;

import java.sql.Date;

public class CommitInfo {

	private String _commit = null;
	private String _committer = null;
	private int _time = 0;
	private String _date = null;
	private String _message = null;

	public CommitInfo(String commit, String committer, int time, String message) {
		_commit = commit;
		_committer = committer;
		_time = time;
		_message = message;

		long longtime = (long) _time * 1000;
		_date = new Date(longtime).toString();
	}

	public String getCommitName() {
		return _commit;
	}

	public String getCommitter() {
		return _committer;
	}

	public String getCommitTime() {
		return _date;
	}

	public String getCommitMessage() {
		return _message;
	}
}
