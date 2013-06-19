package FindBugsManager.DataSets;

public class BugData {

	private String _category = null;
	private String _abbrev = null;
	private String _type = null;
	private int _rank = 0;
	private int _point = 0;
	private String _priority = null;
	private String _condition = null;
	private String _line = null;
	private String _fixer = null;
	private String _author = null;

	public BugData(String category, String abbrev, String type, String rank, String point,
			String priority, String condition, String line, String fixer, String author) {
		_category = category;
		_abbrev = abbrev;
		_type = type;
		_rank = Integer.parseInt(rank);
		_point = Integer.parseInt(point);
		_priority = priority;
		_condition = condition;
		_line = line;
		_fixer = fixer;
		_author = author;

	}

	public BugData(BugInstanceSet info, int bonus) {
		_category = info.getBugInstance().getBugPattern().getCategory();
		_abbrev = info.getBugInstance().getAbbrev();
		_type = info.getBugInstance().getType();
		_rank = info.getBugInstance().getBugRank();
		_point = (21 - _rank) * bonus;
		String priorityString = info.getBugInstance().getPriorityString();
		if (priorityString.equals("優先度(高)")) {
			_priority = "High";
		} else if (priorityString.equals("優先度(中)")) {
			_priority = "Middle";
		} else {
			_priority = "Low";
		}
		_condition = info.getEditType().toString();

		_fixer = info.getAmender();
		_author = info.getAuthor();
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

	public String getCondition() {
		return _condition;
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
