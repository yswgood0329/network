package chat.win;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class User {
	private String name = null;
	private BufferedReader bufferedReader;	
	private PrintWriter printWriter;
	
	public User(BufferedReader bufferedReader, PrintWriter printWriter) {
		this.bufferedReader = bufferedReader;
		this.printWriter = printWriter;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PrintWriter getPrintWriter() {
		return printWriter;
	}

	public void setWriter(PrintWriter printWriter) {
		this.printWriter = printWriter;
	}

	public BufferedReader getBufferedReader() {
		return bufferedReader;
	}

	public void setBufferedReader(BufferedReader bufferedReader) {
		this.bufferedReader = bufferedReader;
	}

}
