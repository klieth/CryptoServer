import java.net.*;
import java.io.*;

public class ServerThread extends Thread {
	Socket s = null;
	public ServerThread(Socket socket) {
		super("ServerThread");
		s = socket;
	}
	public void run() {
		System.out.println("Hello!");
	}
}
