import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import informations.ClientInfo;
import informations.CommandSet;

public class WorkingTread extends Thread {
	private ClientInfo client;
	private PrintWriter pw;
	private BufferedReader br;
	private LinkedList<ClientInfo> users;
	private DBManager db;
	private CommandSet cs;
	private HashMap<String,ArrayList<String>>cm;
	public WorkingTread(Socket s, LinkedList<ClientInfo> users, DBManager db, CommandSet cs) {
		this.users = users;
		this.db = db;
		this.cs = cs;
		cm = cs.getCommandMap();
		try {
			pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream(), "UTF8"));
			br = new BufferedReader(new InputStreamReader(s.getInputStream(), "UTF8"));
			client = new ClientInfo(s, br.readLine());
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
		System.out.println("Access: " + client.getCs().getInetAddress() + "@" + client.getId());
		printMain();
		while (true) {
			pw.println(">> ");
			pw.flush();
			try {
				s = br.readLine();
				System.out.println("Client: " + client.getId() + " enterd msg: " + s);
				if (s == null || s.isEmpty())
					continue;
				if (s.equals("종료") || s.equals("접속종료"))
					break;
				else if (s.equals("동시접속자") || s.equals("동접자"))
					checkConcurrentUsers();
				else {
					if (!processMsg(s)) {
						pw.println("이해하지 못하는 명령어 입니다!");
						pw.flush();
					}else {
						pw.println("The command is accepted.");
						pw.flush();
					}
				}
			} catch (Exception e) {
				break;
			}
		}
		try {
			synchronized (users) {
				users.remove(client);
			}
			client.getCs().close();
			br.close();
			pw.close();
			System.out.println("Client: " + client.getCs().getInetAddress() + "@" + client.getId() + " closed");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean processMsg(String msg) {
		Iterator<ArrayList<String>> iterator;
		Collection<ArrayList<String>>leaf = cm.values();
		iterator = leaf.iterator();
		String[] splitedMgs = msg.split("\\s+");
		for (int i = 0; i < splitedMgs.length; i++) {
			System.out.println("\tLOG: proceesMGS() splite message.");
			System.out.println("\tLOG: [" + i + "] = " + splitedMgs[i] + ".");
		}
		
		for(String s :splitedMgs) {
			while(iterator.hasNext()) {
				
			}
			
		}
		
		return false;
	}

	private void checkConcurrentUsers() {
		int n;
		synchronized (users) {
			n = users.size();
			pw.println("--현재 챗봇 접속자 수: " + n + "명");
			for (ClientInfo s : users)
				pw.println("----ip@id: " + s.getCs().getInetAddress() + "@" + s.getId());
			pw.flush();
		}
	}

	private void printMain() {
		pw.println("--영화관 상영시간표 제공 챗봇에 접속하신걸 환영합니다!");
		pw.println("----명령어를 입력해 주세요.");
		pw.flush();
	}
}
