import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientMain {
	static final String ipAdress = "54.180.98.17";
	
	public static void main(String[] args) {
		Socket s = null;
		BufferedReader br = null;
		try {
			System.out.println("Test to connect server...");
			s = new Socket(ipAdress, 9001);
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			System.out.println(br.readLine());
			System.out.println("Server connection closed");
			System.out.println("hello");
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
