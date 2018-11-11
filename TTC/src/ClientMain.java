import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.Buffer;
import java.util.Scanner;

public class ClientMain {
	static final String ipAdress = "54.180.98.17";
	
	public static void main(String[] args) {
		Socket s = null;
		BufferedReader br = null;
		PrintWriter pw = null;
		String msg = null;
		Scanner sc = new Scanner(System.in);
		try {
			System.out.println("Test to connect server...");
			s = new Socket(ipAdress, 9001);
			br = new BufferedReader(new InputStreamReader(s.getInputStream(), "UTF8"));
			pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream(), "UTF8"));
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
