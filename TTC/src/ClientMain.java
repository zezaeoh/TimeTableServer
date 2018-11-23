import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientMain {
//	static final String ipAdress = "54.180.98.17";
	static final String ipAdress = "127.0.0.1";
	
	public static void main(String[] args) {
		Socket s = null;
		BufferedReader br = null;
		PrintWriter pw = null;
		String msg = null;
		Scanner sc = new Scanner(System.in);
		try {
			System.out.print("서버에 접속할 아이디를 입력하세요: ");
			String id = sc.nextLine();
			System.out.println("서버에 연결 중...");
			
			s = new Socket(ipAdress, 9001);
			br = new BufferedReader(new InputStreamReader(s.getInputStream(), "UTF8"));
			pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream(), "UTF8"));
			pw.println(id);
			pw.flush();
			
			System.out.println("성공적으로 연결되었습니다!");
			
			ReadThread rt = new ReadThread(s, br);
			rt.start();
			while(true) {
				msg = sc.nextLine();
				if(msg != null) {
					pw.println(msg);
					pw.flush();
					if(msg.equals("종료") || msg.equals("접속종료"))
						break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sc.close();
			try {
				s.close();
				pw.close();
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

class ReadThread extends Thread {
	Socket s = null;
	BufferedReader br = null;
	public ReadThread(Socket s, BufferedReader br) {
		this.s = s;
		this.br = br;
	}
	
	public void run() {
		try {
			String input = null;
			while(true) {
				if((input = br.readLine()) != null) {
					if(input.equals(">> "))
						System.out.print(input);
					else
						System.out.println(input);
				}
			}
		} catch(Exception e) {
		} finally {
			try {
				s.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
