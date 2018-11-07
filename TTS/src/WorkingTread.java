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
		pw.println("Server: Responce test okay!");
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
