package FindBugsManager.Git;

import java.util.ArrayList;

import FindBugsManager.DataSets.BugData;
import FindBugsManager.DataSets.BugInstanceSet;
import FindBugsManager.DataSets.PersonalData;

public class AccountManager {

	private static AccountManager instance = new AccountManager();

	private ArrayList<PersonalData> dataList = new ArrayList<PersonalData>();

	private AccountManager() {

	}

	public void addPersonalData(PersonalData newData) {
		dataList.add(newData);
	}

	public void updatePersonalData(String name, int point, ArrayList<BugInstanceSet> bugList) {
		for (PersonalData data : dataList) {
			String comp = data.getName();
			if (comp.equals(name)) {
				data.addPoint(point);
				data.addFixedList(bugList);
				return;
			}
		}
		addPersonalData(new PersonalData(name, point, bugList));
	}

	public ArrayList<PersonalData> getPersonalDataList() {
		return dataList;
	}

	public PersonalData getPersonalData(String name) {
		PersonalData pdata = null;
		for (PersonalData data : dataList) {
			if (data.getName().equals(name)) {
				pdata = data;
				break;
			}
		}
		return pdata;
	}

	public ArrayList<BugData> getPersonalBugData(String name) {
		ArrayList<BugData> bugData = new ArrayList<BugData>();
		PersonalData data = getPersonalData(name);
		ArrayList<BugInstanceSet> bugList = data.getInstanceList();
		for (BugInstanceSet info : bugList) {
			bugData.add(new BugData(info));
		}
		return bugData;
	}

	public static AccountManager getInstance() {
		return instance;
	}

}
