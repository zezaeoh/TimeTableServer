import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import informations.BranchInfo;
import informations.BranchInfoRoot;
import informations.QueryInfo;

public class DBManager {
	private final String JDBC_DRIVER = Settings.getJdbcDriver();
	private final String DB_URL = Settings.getDbUrl();
	private final String USER_NAME = Settings.getUserName();
	private final String PASSWORD = Settings.getPassword();
	
	private final String[][] ths = {{"100", "CGV"}, {"200", "롯데시네마"}};
	
	private static Connection conn = null;
	private static HashMap<String, BranchInfoRoot> branchInfos = null;
	
	private Statement state = null;
	
	
	public DBManager() {
		try {
			Class.forName(JDBC_DRIVER);
			if(conn == null) {
				String sql = null;
				BranchInfoRoot root = null;
				ResultSet rs = null;
				branchInfos = new HashMap<>();
				
				conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
				state = conn.createStatement();
				for(String[] s : ths) {
					sql = String.format("select br_name, br_id from th_branch where th_id = %s", s[0]);
					root = new BranchInfoRoot(s[1]);
					
					rs = state.executeQuery(sql);
					
					while(rs.next())
						root.addInfo(rs.getString(1), rs.getInt(2), Integer.parseInt(s[0]));
				
					branchInfos.put(s[1], root);
				}
			}else
				state = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static public void close() {
		if(conn != null)
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
	
	public void instanceClose() {
		if(state != null)
			try {
				state.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
	
	public BranchInfo getBranchName(String th_name, String msg){ // 영화관 이름이 존재하고 정확한 지점 이름이 들어올 때 매칭된 branchInfo를 리턴한다.
		BranchInfoRoot root = branchInfos.get(th_name);
		if(root == null)
			return null;
		for(BranchInfo bi : root.getList()) {
			if(bi.getBrName().equals(msg))
				return bi;
		}
		return null;
	}
	
	public List<BranchInfo> getBranchNames(String msg){ // 특정 지역에 대한 String이 들어왔을 때 해당 지역에 해당하는 모든 branchInfo를 List형태로 리턴한다.
		LinkedList<BranchInfo> rl = new LinkedList<>();

		for(String s: branchInfos.keySet())
			for(BranchInfo bi: branchInfos.get(s).getList())
				if(bi.getBrName().contains(msg))
					rl.add(bi);
			
		return rl;			
	}
	
	public List<String> getQueryResult(QueryInfo qi){ // 쿼리 info를 받아서 해당되는 쿼리를 수행 한 후 결과를 리턴한다. 해석할 수 없는경우 null을 리턴한다.
		if(qi.getCommand().equals("상영시간표")) {
			
		}
		return null;
	}
	
	private ResultSet getBranchTimeTable(QueryInfo qi) {
		String sql = 
				"select mv_name, mv_time" +
				"from th_mv_times" +
				"where th_id = %s && br_id = %s";
				
		return null;
	}
}
