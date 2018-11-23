import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

	private final String[][] ths = { { "100", "CGV" }, { "200", "롯데시네마" } };

	private static Connection conn = null;
	private static HashMap<String, BranchInfoRoot> branchInfos = null;

	private Statement state = null;

	public DBManager() {
		try {
			Class.forName(JDBC_DRIVER);
			if (conn == null) {
				String sql = null;
				BranchInfoRoot root = null;
				ResultSet rs = null;
				branchInfos = new HashMap<>();

				conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
				state = conn.createStatement();
				for (String[] s : ths) {
					sql = String.format("select br_name, br_id from th_branch where th_id = %s", s[0]);
					root = new BranchInfoRoot(s[1]);

					rs = state.executeQuery(sql);

					while (rs.next())
						root.addInfo(rs.getString(1), rs.getInt(2), Integer.parseInt(s[0]));

					branchInfos.put(s[1], root);
				}
			} else
				state = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static public void close() {
		if (conn != null)
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}

	public void instanceClose() {
		if (state != null)
			try {
				state.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}

	public BranchInfo getBranchName(String th_name, String msg) { // 영화관 이름이 존재하고 정확한 지점 이름이 들어올 때 매칭된 branchInfo를 리턴한다.
		BranchInfoRoot root = branchInfos.get(th_name);
		if (root == null)
			return null;
		for (BranchInfo bi : root.getList()) {
			if (bi.getBrName().equals(msg))
				return bi;
		}
		return null;
	}

	public List<BranchInfo> getBranchNames(String msg) { // 특정 지역에 대한 String이 들어왔을 때 해당 지역에 해당하는 모든 branchInfo를 List형태로
															// 리턴한다.
		LinkedList<BranchInfo> rl = new LinkedList<>();

		for (String s : branchInfos.keySet())
			for (BranchInfo bi : branchInfos.get(s).getList())
				if (bi.getBrName().contains(msg))
					rl.add(bi);

		return rl;
	}

	public List<String> getQueryResult(QueryInfo qi) { // 쿼리 info를 받아서 해당되는 쿼리를 수행 한 후 결과를 리턴한다. 해석할 수 없는경우 null을 리턴한다.
		ResultSet rs = null;
		LinkedList<String> reMsgs = null;
		StringBuffer sb = null;
		LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, LinkedList<String>>>> dict = null;
		String th_name, br_name, mv_title, mv_time;
		th_name = br_name = mv_title = mv_time = null;
		
		try {
			if (qi.getCommand().equals("상영시간표")) {
				rs = getTimeTable(qi);
				if(rs == null)
					return null;
				
				dict = new LinkedHashMap<>();
				reMsgs = new LinkedList<>();
				sb = new StringBuffer();
				
				while(rs.next()) {
					th_name = rs.getString(1);
					br_name = rs.getString(2);
					mv_title = rs.getString(3);
					mv_time = rs.getString(4);
					
					if(!dict.containsKey(th_name))
						dict.put(th_name, new LinkedHashMap<>());
					if(!dict.get(th_name).containsKey(br_name))
						dict.get(th_name).put(br_name, new LinkedHashMap<>());
					if(!dict.get(th_name).get(br_name).containsKey(mv_title))
						dict.get(th_name).get(br_name).put(mv_title, new LinkedList<>());
					
					dict.get(th_name).get(br_name).get(mv_title).add(mv_time.substring(11));
				}
				
				for(String tn : dict.keySet()) {
					reMsgs.add(tn);
					for(String bn: dict.get(tn).keySet()) {
						reMsgs.add("  " + bn);
						reMsgs.add(" ");
						for(String mn: dict.get(tn).get(bn).keySet()) {
							reMsgs.add("    " + mn);
							for(String mt: dict.get(tn).get(bn).get(mn))
								sb.append(mt + " ");
							reMsgs.add(sb.toString());
							reMsgs.add(" ");
							sb.setLength(0);
						}
					}
				}
				
				if(reMsgs.isEmpty())
					return null;
				return reMsgs;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	private ResultSet getTimeTable(QueryInfo qi) {
		List<BranchInfo> li = null;
		ResultSet rs = null;
		String sql = null;
		StringBuffer sb = null;
		
		try {
			if(qi.haveMvTime()) {
				if(!qi.haveThBrId())
					return null;
				
				li = qi.getThBrIds();
				sb = new StringBuffer();
				
				sql = "select th_name, br_name, mv_title, mv_time " +
					  "from th_info t, th_branch b, (" +
						"select th_id, br_id, mv_title, mv_time " +
						"from th_mv_times " +
						"where mv_time like '%s' && (%s)" +
					  ") m " +
					  "where m.th_id = t.th_id && m.br_id = b.br_id && t.th_id = b.th_id " +
					  "order by mv_time";
				
				for(int i=0; i<li.size(); i++) {
					if(i == 0)
						sb.append(String.format("(th_id = %d && br_id = %d)", li.get(i).getThId(), li.get(i).getBrId()));
					else
						sb.append(String.format(" || (th_id = %d && br_id = %d)", li.get(i).getThId(), li.get(i).getBrId()));
				}
				
				sql = String.format(sql, qi.getMvTime(), sb.toString());
				rs = state.executeQuery(sql);
			}else if(qi.haveThId()) {
				li = qi.getThBrIds();
				if(li.size() != 1)
					return null;
				
				sql = "select th_name, br_name, mv_title, mv_time " +
					  "from th_mv_times m, th_branch b, th_info t " +
					  "where t.th_id = b.th_id && b.br_id = m.br_id &&" +
					    " t.th_id = m.th_id && m.th_id = %d && m.br_id = %d &&" +
					    " m.mv_time like '%s' " +
					  "order by mv_time";
				sql = String.format(sql, qi.getThId(), li.get(0).getBrId(), qi.getDate());
				rs = state.executeQuery(sql);
			}
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return rs;
	}
}
