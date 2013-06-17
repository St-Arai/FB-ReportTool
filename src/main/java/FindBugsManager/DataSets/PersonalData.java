package FindBugsManager.DataSets;

import java.util.ArrayList;

public class PersonalData {

	private int next = 100;
	private int _point = 0;
	private String _name = null;
	// private String _name = "Satoshi Arai(Lab)";
	private String job = "Beginner";
	private int level = 1;

	private ArrayList<BugInstanceSet> fixedList = new ArrayList<BugInstanceSet>();

	public PersonalData() {

	}

	public PersonalData(String name) {
		_name = name;
	}

	public PersonalData(String name, int point, ArrayList<BugInstanceSet> fixedList) {
		_name = name;
		addPoint(point);
		addFixedList(fixedList);
	}

	public int getRemain() {
		return next;
	}

	public int getPoint() {
		return _point;
	}

	public String getName() {
		return _name;
	}

	public String getJob() {
		return job;
	}

	public int getLevel() {
		return level;
	}

	public ArrayList<BugInstanceSet> getInstanceList() {
		return fixedList;
	}

	public void addFixedList(ArrayList<BugInstanceSet> list) {
		fixedList.addAll(list);
	}

	public void addPoint(int point) {
		_point += point;
	}
}
