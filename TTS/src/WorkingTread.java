import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class WorkingTread extends Thread{
	private Socket client;
	private PrintWriter pw;
	public WorkingTread(Socket s) {
		client = s;
		try {
			pw = new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		System.out.println("Access: " + client.getInetAddress());
		pw.println("Server: Responce test okay!");
		pw.flush();
		try {
			client.close();
			System.out.println("Client closed");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
