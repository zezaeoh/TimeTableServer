package informations;

public class BranchInfo {
	private String brName;
	private int brId;
	
	public BranchInfo(String brName, int brId) {
		this.brName = brName;
		this.brId = brId;
	}

	public String getBrName() {
		return brName;
	}

	public int getBrId() {
		return brId;
	}
}
