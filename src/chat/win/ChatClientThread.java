package chat.win;

import java.io.BufferedReader;
import java.io.IOException;

public class ChatClientThread extends Thread {
	private ChatWindow chatWindow;
	private BufferedReader bufferedReader;
	
	public ChatClientThread(ChatWindow chatWindow, BufferedReader bufferedReader) {
		this.chatWindow = chatWindow;
		this.bufferedReader = bufferedReader;
	}
	
	@Override
	public void run() {
		chatWindow.show();
	
		
		String message;
		try {
			while(true) {
				message = bufferedReader.readLine();
				if("exit".equals(message)) {
					System.exit(0);
				}
				String[] tokens = message.split(":");
				System.out.println(tokens[1]+":"+tokens[0]);
				chatWindow.receiveMessage(tokens[1], tokens[0]);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
