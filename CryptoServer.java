import java.net.*;
import java.io.*;

public class CryptoServer {
	static int port = 4444;

	public static void main(String[] args) {
		ServerSocket ss = null;
		boolean listening = true;

		try {
			ss = new ServerSocket(3333);
			
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
