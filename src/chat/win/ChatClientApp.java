package chat.win;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class ChatClientApp{

//	SERVER_IP = 로컬 IP 확인 필요
	private static final String SERVER_IP = "192.168.0.93";
	private static final int SERVER_PORT = 9999;
	
	public static void main(String[] args) {
		String name = null;
		Scanner scanner = new Scanner(System.in);
		Socket socket = null;
		ChatWindow cw = null;
		
		try {
			socket = new Socket();
			
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
			
		
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true); // ??? 마지막에 true는 무엇?
			printWriter.println("connected");
	
			while( true ) {
				
				System.out.println("대화명을 입력하세요.");
				System.out.print(">>> ");
				name = scanner.nextLine();
				
				if (name.isEmpty() == false ) {
					cw = new ChatWindow(name, printWriter);
					printWriter.println("JOIN:" + name);
					printWriter.flush();
					break;
				}
				
				System.out.println("대화명은 한글자 이상 입력해야 합니다.\n");
			}
			
			Thread thread = new ChatClientThread(cw, bufferedReader);
			thread.start();
			
			while(true) {
				
			}
			
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
		scanner.close();
		
//		
//		JOIN 처리
//		Response가 "JOIN:OK" 이면
//		

//		ChatWindow cw = new ChatWindow(name);
		
//		new ChatClientThread(cw).start();
		
//		cw.show();
	}

}
