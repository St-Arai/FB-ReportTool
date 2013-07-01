package FindBugsManager.Git;

import java.util.ArrayList;

import FindBugsManager.DataSets.BugData;
import FindBugsManager.DataSets.PersonalData;

public class AccountManager {

	private static AccountManager instance = new AccountManager();

	private ArrayList<String> nameList = new ArrayList<String>();

	private ArrayList<PersonalData> pDataList = new ArrayList<PersonalData>();

	private AccountManager() {

	}

	public void addPersonalData(PersonalData newData) {
		if (!(nameList.contains(newData.getName()))) {
			pDataList.add(newData);
			nameList.add(newData.getName());
		}
	}

	public ArrayList<String> getNameList() {
		return nameList;
	}

	public void updatePersonalData(String name, ArrayList<BugData> bugList, int missCount) {
		for (PersonalData data : pDataList) {
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
		return pDataList;
	}

	public PersonalData getPersonalData(String name) {
		PersonalData pdata = null;
		for (PersonalData data : pDataList) {
			if (data.getName().equals(name)) {
				pdata = data;
				break;
			}
		}
		return pdata;
	}

	public void allocateAllBugData(ArrayList<BugData> info) {
		initAllPersonalData();
		for (PersonalData pData : pDataList) {
			String name = pData.getName();
			for (BugData data : info) {
				String fixer = data.getFixer();
				if (name.equals(fixer)) {
					pData.addFixerData(data);
				}
			}
		}
	}

	public ArrayList<BugData> getPersonalBugDataList(String name) {
		PersonalData data = getPersonalData(name);
		ArrayList<BugData> bugList = data.getInstanceList();
		return bugList;
	}

	public void initAllPersonalData() {
		for (PersonalData data : pDataList) {
			data.initFixedData();
		}
	}

	public static AccountManager getInstance() {
		return instance;
	}

}
