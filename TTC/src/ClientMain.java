import java.io.IOException;
import java.net.Socket;

public class ClientMain {
	static final String ipAdress = "54.180.98.17";
	
	public static void main(String[] args) {
		Socket s = null;
		try {
			System.out.println("Test to connect server...");
			s = new Socket(ipAdress, 9001);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				s.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
