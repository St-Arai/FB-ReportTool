package FindBugsManager.Git;

import java.util.ArrayList;

import FindBugsManager.DataSets.BugData;
import FindBugsManager.DataSets.PersonalData;

public class AccountManager {

	private static AccountManager instance = new AccountManager();

	private ArrayList<String> nameList = new ArrayList<String>();

	private ArrayList<PersonalData> dataList = new ArrayList<PersonalData>();

	private AccountManager() {

	}

	public void addPersonalData(PersonalData newData) {
		dataList.add(newData);
		if (!(nameList.contains(newData.getName()))) {
			nameList.add(newData.getName());
		}
	}

	public ArrayList<String> getNameList() {
		return nameList;
	}

	public void updatePersonalData(String name, ArrayList<BugData> bugList, int missCount) {
		for (PersonalData data : dataList) {
			String comp = data.getName();
			if (comp.equals(name)) {
				data.addFixedList(bugList);
				data.addMissCount(missCount);
				return;
			}
		}
		addPersonalData(new PersonalData(name, bugList, missCount));
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

	public ArrayList<BugData> getPersonalBugDataList(String name) {
		PersonalData data = getPersonalData(name);
		ArrayList<BugData> bugList = data.getInstanceList();
		return bugList;
	}

	public static AccountManager getInstance() {
		return instance;
	}

}
