import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class ServerMain {
	public static void main(String[] args) {
		ServerSocket server = null;
		Socket s = null;
		LinkedList<Socket> users = null;
		try {
			server = new ServerSocket(9001);
			users = new LinkedList<>();
			while (true) {
				System.out.println("wait for client...");
				s = server.accept();
				WorkingTread wt = new WorkingTread(s, users);
				wt.run();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (server != null)
					server.close();
				if (users != null) {
					synchronized (users) {
						for (Socket ss : users) {
							if (ss != null)
								ss.close();
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
