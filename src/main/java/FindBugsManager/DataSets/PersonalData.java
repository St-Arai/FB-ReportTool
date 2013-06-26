package FindBugsManager.DataSets;

import java.util.ArrayList;

public class PersonalData {

	private int next = 100;
	private String _name = null;
	private String job = "Beginner";
	private int level = 1;
	private int _missCount = 0;

	private ArrayList<BugData> fixedList = new ArrayList<BugData>();
	private ArrayList<String> nameList = new ArrayList<String>();

	public PersonalData() {

	}

	public PersonalData(String name) {
		_name = name;
	}

	public PersonalData(String name, ArrayList<BugData> fixedList, int missCount) {
		_name = name;
		addFixedList(fixedList);
		addMissCount(missCount);
	}

	public int getRemain() {
		return next;
	}

	public ArrayList<String> getNameList() {
		return nameList;
	}

	public int getTotalPoint() {
		int total = 0;
		for (BugData data : fixedList) {
			total += data.getPoint();
		}
		return total;
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

	public int getMissCount() {
		return _missCount;
	}

	public ArrayList<BugData> getInstanceList() {
		return fixedList;
	}

	public void addFixedList(ArrayList<BugData> list) {
		fixedList.addAll(list);
	}

	public void addMissCount(int missCount) {
		_missCount += missCount;
	}
}
