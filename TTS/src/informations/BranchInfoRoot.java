package informations;

import java.util.LinkedList;

public class BranchInfoRoot {
	private final String thName;
	private LinkedList<BranchInfo> list;
	
	public BranchInfoRoot(String thName) {
		this.thName = thName;
		list = new LinkedList<>();
	}
	
	public void addInfo(String brName, int brId) {
		list.add(new BranchInfo(brName, brId));
	}

	public String getThName() {
		return thName;
	}

	public LinkedList<BranchInfo> getList() {
		return list;
	}
	
}
