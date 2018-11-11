import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;

public class WorkingTread extends Thread{
	private Socket client;
	private PrintWriter pw;
	private BufferedReader br;
	private LinkedList<Socket> users;
	
	public WorkingTread(Socket s, LinkedList<Socket> users) {
		client = s;
		this.users = users;
		try {
			pw = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), "UTF8"));
			br = new BufferedReader(new InputStreamReader(client.getInputStream(),"UTF8"));
			synchronized (users) {
				users.add(client);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		String s = null;
		System.out.println("Access: " + client.getInetAddress());
		printMain();
		while(true) {
			pw.println(">>");
			pw.flush();
			try {
				s = br.readLine();
				if(s.equals("종료") || s.equals("접속종료"))
					break;
				else if(s.equals("동시접속자") || s.equals("동접자"))
					checkConcurrentUsers();
				else
					pw.println("이해하지 못하는 명령어 입니다!");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			client.close();
			System.out.println("Client closed");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void checkConcurrentUsers() {
		int n;
		synchronized (users) {
			n = users.size();
			pw.println("--현재 챗봇 접속자 수: " + n + "명");
			for(Socket s: users)
				pw.println("----ip: " + s.getInetAddress());
			pw.flush();
		}
	}

	private void printMain() {
		pw.println("--영화관 상영시간표 제공 챗봇에 접속하신걸 환영합니다!");
		pw.println("----명령어를 입력해 주세요.");
		pw.flush();
	}
}
