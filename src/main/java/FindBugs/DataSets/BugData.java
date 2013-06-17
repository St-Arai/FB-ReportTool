package FindBugs.DataSets;

public class BugData {

	private String _category = null;
	private String _abbrev = null;
	private String _type = null;
	private int _rank = 0;
	private int _point = 0;
	private String _priority = null;
	private String _line = null;
	private String _fixer = null;
	private String _author = null;

	public BugData(String category, String abbrev, String type, String rank, String point,
			String priority, String line, String fixer, String author) {
		_category = category;
		_abbrev = abbrev;
		_type = type;
		_rank = Integer.parseInt(rank);
		_point = Integer.parseInt(point);
		_priority = priority;
		_line = line;
		_fixer = fixer;
		_author = author;

	}

	public String getCategory() {
		return _category;
	}

	public String getAbbrev() {
		return _abbrev;
	}

	public String getType() {
		return _type;
	}

	public int getRank() {
		return _rank;
	}

	public int getPoint() {
		return _point;
	}

	public String getPriority() {
		return _priority;
	}

	public String getLine() {
		return _line;
	}

	public String getFixer() {
		return _fixer;
	}

	public String getAuthor() {
		return _author;
	}
}
