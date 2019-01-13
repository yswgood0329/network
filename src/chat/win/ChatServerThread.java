package chat.win;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

public class ChatServerThread extends Thread {
	private Socket socket;
	private User user;
	private List<User> userPool;
//	private PrintWriter printWriter;
//	private BufferedReader bufferedReader;
	
	public ChatServerThread(Socket socket, List<User> userPool) {
		this.socket = socket;
		this.userPool = userPool;
	}

	@Override
	public void run() {
		
		InetSocketAddress inetRemoteSocketAddress = (InetSocketAddress)socket.getRemoteSocketAddress();
		ChatServer.log("connected by client[" + inetRemoteSocketAddress.getAddress().getHostAddress() + " : " + inetRemoteSocketAddress.getPort() + "]");

		try {
			user = new User(new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8")), new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true));
//			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));	// ?? 이부분을 잘 모르겠네????
//			printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);	// ?? 이부분을 잘 모르겠네????
			
			while(true) {
				String line = user.getBufferedReader().readLine();
				ChatServer.log(line);
				if(line == null) {
					ChatServer.log("closed by client");
					doBye(user);
					break;
				}
				
//				프로토콜 분석
				/* 
				 * 1. JOIN : 로그인		JOIN:nickname
				 * 2. MSG : 메세지 내용		MSG:sendNickname:line
				 * 3. SMSG : 귓속말		SMSG:sendNickname:receiveNickname:line
				 * 4. BAN : 강퇴			BAN:banNickname
				 * 3. BYE : 연결 종료		BYE:
				 * 토큰은 :을 사용하고 완성 후 base64 인코딩을 통해 문제점을 해결한다.
				 */
				
				String[] tokens = line.split(":");
				switch(tokens[0]) {
				case("JOIN"):
					System.out.println(user.getName());
					doJoin(tokens[1]);
					break;
					
				case("MSG"):
					doMsg(tokens[1],user);
					break;
					
				case("SMSG"):
					doSmsg(tokens[1], tokens[2], tokens[3], user);
					break;
					
				case("BAN"):
					doBan();
					break;
				
				case("BYE"):
					doBye(user);
					break;
				
				default:
					break;
				
				}
				
			}
			
		}catch(SocketException e) {
			removeWriter(user);
			ChatServer.log("사용자가 나감, 종료");
		}catch(IOException e) {
			ChatServer.log("error : " + e);
		}
	}
	
	private void doJoin(String nickname) {
		user.setName(nickname);
		user.getPrintWriter().println("JOIN:OK");
		user.getPrintWriter().flush();
		broadCasting("님이 들어왔습니다.", nickname);

		addWriter(user);
	}
	
	private void doMsg(String line, User user) {
		broadCasting(line, user.getName());
	}
	
	private void doBye(User user) {
		user.getPrintWriter().println("exit");
		user.getPrintWriter().flush();
		removeWriter(user);
		
		broadCasting("님이 나갔습니다.",user.getName());
	}
	
	private void doSmsg(String line, String sendNickname, String receiveNickname, User user) {
		// 이거는 연결된 소켓하고 nickname이 연동이 되어 있어야 되겠는데
		// 수정 하기 너무 귀찮다.
		user.getPrintWriter().println(line+":"+sendNickname);
		user.getPrintWriter().flush();
	}
	
	private void doBan() {}
	
	private void addWriter(User user) {
		synchronized(userPool) {
			userPool.add(user);
		}
	}
	
	private void removeWriter(User user) {
		synchronized(userPool) {
			userPool.remove(user);
		}
	}
	
	private void broadCasting(String data, String sendNickname) {
		System.out.println("BroadCasting...");
		synchronized(userPool) {
			for(User user : userPool) {
				System.out.println(user);
				PrintWriter printWriter = user.getPrintWriter();
				printWriter.println(data+":"+sendNickname);
				printWriter.flush();
			}
		}
	}

}
