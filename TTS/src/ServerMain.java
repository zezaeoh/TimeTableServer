import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class ServerMain {
	public static void main(String[] args) {
		ServerSocket server = null;
		Socket s = null;
		LinkedList<ClientInfo> users = null;
		try {
			server = new ServerSocket(9001);
			users = new LinkedList<>();
			while (true) {
				System.out.println("wait for client...");
				s = server.accept();
				new WorkingTread(s, users, new DBManager()).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (server != null)
					server.close();
				if (users != null) {
					synchronized (users) {
						for (ClientInfo ss : users) {
							if (ss.getCs() != null)
								ss.getCs().close();
						}
					}
				}
				DBManager.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
