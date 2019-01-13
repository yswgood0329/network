package chat.win;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
	private static final int PORT = 9999;
	public static List<User> userPool = new ArrayList<User>();
	
	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		
		try {
			serverSocket = new ServerSocket();
			
//			1-1. set option SO_REUSEADDR
//			(종료후 빨리 바인딩을 하기 위해서)
			serverSocket.setReuseAddress(true);
			
			
			String localHostAddress = InetAddress.getLocalHost().getHostAddress();
			serverSocket.bind(new InetSocketAddress(localHostAddress, PORT));
			log("binding " + localHostAddress + ":" + PORT);
			
			while(true) {
				Socket socket = serverSocket.accept();
				Thread thread = new ChatServerThread(socket, userPool);
				thread.start();
			}
			
		} catch (IOException e) {
			log("error : " + e);
		}

	}
	
	public static void log(String log) {
		System.out.println("<server> " + log);
	}

}
