package FindBugsManager.Git;

import java.sql.Date;

public class CommitInfo {

	private String _commit = null;
	private String _author = null;
	private int _time = 0;
	private String _date = null;
	private String _message = null;

	public CommitInfo(String commit, String author, int time, String message) {
		_commit = commit;
		_author = author;
		_time = time;
		_message = message;

		long longtime = (long) _time * 1000;
		_date = new Date(longtime).toString();
	}

	public String getCommitName() {
		return _commit;
	}

	public String getAuthor() {
		return _author;
	}

	public String getCommitTime() {
		return _date;
	}

	public String getCommitMessage() {
		return _message;
	}
}
