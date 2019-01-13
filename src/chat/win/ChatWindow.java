package chat.win;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintWriter;

public class ChatWindow {

	private Frame frame;
	private Panel pannel;
	private Button buttonSend;
	private TextField textField;
	private TextArea textArea;
	private String name;
	private PrintWriter printWriter;

	public ChatWindow(String name, PrintWriter printWriter) {
		this.name = name;
		this.printWriter = printWriter;
		frame = new Frame(name);
		pannel = new Panel();
		buttonSend = new Button("Send");
		textField = new TextField();
		textArea = new TextArea(30, 80);
	}

	public void show() {
		// Button
		buttonSend.setBackground(Color.GRAY);
		buttonSend.setForeground(Color.WHITE);
		buttonSend.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent actionEvent ) {
				sendMessage();
			}
		});
		
//		buttonSend.addActionListener( e -> {}); 람다식 / 함수형 인터페이스(하나의 함수만 들어있는  인터페이스)

		// Textfield
		textField.setColumns(80);
		textField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				char keyCode = e.getKeyChar();
				if(keyCode == KeyEvent.VK_ENTER) {
					sendMessage();
				}
			}
		});

		// Pannel
		pannel.setBackground(Color.LIGHT_GRAY);
		pannel.add(textField);
		pannel.add(buttonSend);
		frame.add(BorderLayout.SOUTH, pannel);

		// TextArea
		textArea.setEditable(false);
		frame.add(BorderLayout.CENTER, textArea);

		// Frame
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		frame.setVisible(true);
		frame.pack();
	}
	
//	private void sendMessage() {
	public void sendMessage() {
		String message = textField.getText();
		if("BYE".equals(message)) {
			printWriter.println("BYE");
			return;
		}
//		MSG 명령 처리 요청
//		"MESSAGE: + message\r\n"
		
//		test(Thread 안에 있어야 하는 코드)
		printWriter.println("MSG:"+name+":"+message);
		textField.setText("");
		textField.requestFocus();
	}
	
	public void receiveMessage(String message, String sendNickname) {
		textArea.append(sendNickname + " : " + message);
		textArea.append("\n");
	}
}
