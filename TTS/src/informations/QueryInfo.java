package informations;

import java.util.LinkedList;
import java.util.List;

public class QueryInfo {
	private String command; // 명령어 입력 ex) 상영시간표
	private int thId; // 영화관 ID 입력 (cgv: 100, lc: 200)
	private LinkedList<BranchInfo> thBrIds; // 지점 id들 입력 !!반드시 BranchInfo instance를 넣을것!!
	private String mvTime; // 영화 시간대 입력 !!반드시 다음 형식으로 넣을것 "2018-08-11 14:%"!!
	
	public QueryInfo() {
		command = null; 
		thId = -1;
		thBrIds = new LinkedList<>();
		mvTime = null;
	}
	
	public boolean haveCommand() {
		if(command == null || command.isEmpty())
			return false;
		return true;
	}
	
	public boolean haveThId() {
		if(thId == -1)
			return false;
		return true;
	}
	
	public boolean haveThBrId() {
		if(thBrIds == null || thBrIds.isEmpty())
			return false;
		return true;
	}
	
	public boolean haveMvTime() {
		if(mvTime == null || mvTime.isEmpty())
			return false;
		return true;
	}
	
	public String getCommand() {
		return command;
	}

	public int getThId() {
		return thId;
	}

	public LinkedList<BranchInfo> getThBrIds() {
		return thBrIds;
	}

	public String getMvTime() {
		return mvTime;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public void setThId(int thId) {
		this.thId = thId;
	}

	public void addThBrId(BranchInfo thBrId) {
		this.thBrIds.add(thBrId);
	}
	
	public void addAllThBrIds(List<BranchInfo> thBrIds) {
		this.thBrIds.addAll(thBrIds);
	}

	public void setMvTime(String mvTime) {
		this.mvTime = mvTime;
	}
}