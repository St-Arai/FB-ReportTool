package FindBugs.DataSets;

public class PersonalData {

	private int next = 100;
	private String name = "Satoshi Arai";
	private String job = "Remodeler";
	private int level = 4;

	public PersonalData() {

	}

	public int getRemain() {
		return next;
	}

	public String getName() {
		return name;
	}

	public String getJob() {
		return job;
	}

	public int getLevel() {
		return level;
	}

}
