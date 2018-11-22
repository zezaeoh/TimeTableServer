import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

import informations.BranchInfo;
import informations.BranchInfoRoot;

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
					
					while(rs.next()) {
						root.addInfo(rs.getString(1), rs.getInt(2));
					}
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
	
	public BranchInfo getBranchName(String th_name, String msg){
		BranchInfoRoot root = branchInfos.get(th_name);
		if(root == null)
			return null;
		for(BranchInfo bi : root.getList()) {
			if(bi.getBrName().equals(msg))
				return bi;
		}
		return null;
	}
	
	public List<String> getBranchTimeTable(){
		
		return null;
	}
}
