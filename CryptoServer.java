import java.net.*;
import java.io.*;

public class CryptoServer {
	static int port = 3333;

	public static void main(String[] args) {
		ServerSocket ss = null;
		boolean listening = true;

		try {
			ss = new ServerSocket(port);
			
			while (listening) {
				new ServerThread(ss.accept()).start();
			}

			ss.close();
		} catch (IOException e) {
			System.err.println("Could not listen on port: " + port);
			System.exit(-1);
		}


	}
}
