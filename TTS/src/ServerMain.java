import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {
	public static void main(String[] args) {
		ServerSocket server = null;
		try {
			server = new ServerSocket(9001);
			while (true) {
				System.out.println("wait for client...");
				Socket s = server.accept();
				WorkingTread wt = new WorkingTread(s);
				wt.run();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (server != null)
				try {
					server.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
}
