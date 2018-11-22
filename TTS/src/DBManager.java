import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {
	private final String JDBC_DRIVER = Settings.getJdbcDriver();
	private final String DB_URL = Settings.getDbUrl();
	private final String USER_NAME = Settings.getUserName();
	private final String PASSWORD = Settings.getPassword();
	
	static Connection conn = null;
	private Statement state = null;
	
	public DBManager() {
		try {
			Class.forName(JDBC_DRIVER);
			if(conn == null)
				conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
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
}
