import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import informations.ClientInfo;
import informations.CommandSet;
import informations.QueryInfo;

public class WorkingTread extends Thread {
	private ClientInfo client;
	private PrintWriter pw;
	private BufferedReader br;
	private LinkedList<ClientInfo> users;
	private DBManager db;
	private HashMap<String, LinkedList<String>> cm;

	public WorkingTread(Socket s, LinkedList<ClientInfo> users, DBManager db, CommandSet cs) {
		this.users = users;
		this.db = db;
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
					if (!processMsg(s)) {// checking the command.
						pw.println("이해하지 못하는 명령어 입니다!");
						pw.flush();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
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
			db.instanceClose();
			System.out.println("Client: " + client.getCs().getInetAddress() + "@" + client.getId() + " closed");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean processMsg(String msg) {
		printLog("test");
		final String command1 = "상영시간표";
		
		LinkedList<String> screeningScheduleLeaf = cm.get(command1);
		LinkedList<String> splitedMsg = new LinkedList<>();
		QueryInfo qi = new QueryInfo(); // 밑으로 내려가면서 조건을 체크하면서 점차 이 인스턴스를 채울 것

		for (String s : msg.split("\\s+"))
			splitedMsg.add(s);
		printLog(splitedMsg.toString());
		
		outerloop:
		for (String sMsg : splitedMsg)
			for (String ssleaf : screeningScheduleLeaf)
				if (sMsg.equals(ssleaf)) {
					printLog("Matched: " + sMsg + "=" + ssleaf);
					splitedMsg.remove(sMsg);
					qi.setCommand(command1); // 명령어가 매치되었으므로 qi에 입력
					break outerloop;
				}
		if(!qi.haveCommand()) // 위 loop를 지나서도 명령어를 매치하지 못했으면 false를 리턴
			return false;
		
		/*
		 *  조건 매칭을 계속 해나가면서 위의 과정을 반복
		 *  조건들: (영화관 이름 & 지점 이름) 또는 (지역 이름 & 영화 시간대) 
		 *  
		 *  조건 매칭에 사용할 수 있는 명령어들:
		 *  1. db.getBranchName(thName, msg)
		 *  	--> 영화관 이름이 찾아진 경우 해당 명령어를 이용하여 영화관 이름과 잘라진 msg들을 넘기면 해당되는 정확한 이름의
		 *          지점이 존재하는 경우 BranchInfo를 리턴한다. 못찾는 경우에는 null을 리턴한다.
		 *  2. db.getBranchNames(msg)
		 *      --> 영화관 이름을 발견하지 못한 경우 지역정보를 확인하기 위해 모든 잘라진 msg들을 넘긴다.
		 *          지역정보가 포함된 지점의 정보들을 모두 확인하여 BranchInfo의 리스트를 리턴한다.
		 *          지점이 존해하지 않는 경우 empty한 리스트를 리턴한다.
		 */
		
		List<String> qr = db.getQueryResult(qi); // 모든 조건이 체크된 qi를 이용하여 쿼리를 날림
		if(qr == null || qr.isEmpty())
			return false;
		
		// 결과로 받은 String들을 client에게 전송
		for(String ss : qr) {
			pw.println(ss);
			pw.flush();
		}
		
		return true;
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

	private void printLog(String msg) {
		System.out.println("\\tLOG: " + msg);
	}
}
