import java.util.*;
import java.net.*;
import java.io.*;

public class ServerThread extends Thread {
	Socket s = null;
	String loggedInUser = null;
	// hack for testing
	List<String> users;
	// ----------------
	public ServerThread(Socket socket) {
		super("ServerThread");
		s = socket;


		// hack for testing
		users = new ArrayList<String>();
		users.add("kai");
		// ----------------
	}
	public void run() {
		try {
			String line;
			PrintWriter out = new PrintWriter(s.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			System.out.println("New client connected");
			out.println("220 Service ready");
			while ((line = in.readLine()) != null) {
				String[] command = line.split(" ");
				System.out.println("Received \"" + line + "\" from client.");
				if (command[0].equals("USER")) {
					if (command.length > 2) {
						out.println("500 Too many arguments");
						break;
					}
					// hack for testing
					for (int i = 0; i < users.size(); i++) {
						if (users.get(i).equals(command[1])) this.loggedInUser = command[1];
					}
					if (this.loggedInUser != null) {
						out.println("230 Logged in");
					} else {
						out.println("500 Unknown user");
						break;
					}
					// ----------------
				} else if (line.startsWith("MESG")) {
				} else if (line.startsWith("DATA")) {
				} else if (line.startsWith("QUIT")) {
				} else {
					out.println("Command \"" + command[0] + "\" was not recognized");
				}
			}
			if (line == null) {
				System.out.println("500 Connection was closed by the client");
				// perform cleanup
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Done!");
	}
}
