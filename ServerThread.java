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
		users.add("bill");
		users.add("bob");
		users.add("tyler");
		// ----------------
	}
	public void run() {
		try {
			String line;
			PrintWriter out = new PrintWriter(s.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			System.out.println("New client connected");
			out.println("220 Service ready");
			boolean inMessage = false;
			String messageRecipient = null;
			String messageData = null;
			while ((line = in.readLine()) != null) {
				String[] command = line.split(" ");
				System.out.println("Received \"" + line + "\" from client.");
				if (command[0].equals("USER")) {
					if (command.length != 2) {
						out.println("500 Wrong number of arguments");
						continue;
					} else if (this.loggedInUser != null) {
						out.println("500 Already logged in");
						continue;
					}
					// hack for testing
					for (int i = 0; i < users.size(); i++) {
						if (users.get(i).equals(command[1])) {
							this.loggedInUser = command[1];
							break;
						}
					}
					if (this.loggedInUser != null) {
						out.println("230 Logged in");
					} else {
						out.println("500 Unknown user");
						continue;
					}
					// ----------------
				} else if (line.startsWith("MESG")) {
					if (command.length != 2) {
						out.println("500 Wrong number of arguments");
						continue;
					} else if (!command[1].startsWith("To:")) {
						out.println("500 Incorrect argument");
						continue;
					} else if (this.loggedInUser == null) {
						out.println("500 No user logged in");
						continue;
					} else if (inMessage) {
						out.println("500 Message already started");
						continue;
					}
					String recipient = command[1].substring(3);
					if (!users.contains(recipient)) {
						out.println("500 Unknown user");
						continue;
					}
					inMessage = true;
					messageRecipient = recipient;
					out.println("250 Ok");
				} else if (line.startsWith("DATA")) {
					if (command.length != 1) {
						out.println("500 Wrong number of arguments");
						continue;
					} else if (this.loggedInUser == null) {
						out.println("500 No user logged in");
						continue;
					} else if (!inMessage) {
						out.println("500 Must start a message before sending data");
						continue;
					}
					out.println("354 Enter message, ending with a “.” on a line by itself.");
					messageData = "";
					String dataLine;
					while (!(dataLine = in.readLine()).equals(".")) {
						messageData += dataLine + "\n";
					}
					out.println("250 Ok");
					System.out.println("Sending message to: " + messageRecipient);
					System.out.println(messageData);
					inMessage = false;
					messageRecipient = null;
					messageData = null;
				} else if (line.startsWith("QUIT")) {
					if (command.length != 1) {
						out.println("500 Wrong number of arguments");
						continue;
					} else if (this.loggedInUser == null) {
						out.println("500 No user logged in");
						continue;
					}
					this.loggedInUser = null;
					inMessage = false;
					messageRecipient = null;
					messageData = null;
					out.println("221 Logged out");
				} else {
					out.println("Command \"" + command[0] + "\" was not recognized");
				}
			}
			if (line == null) {
				System.out.println("Connection was closed by the client");
				// perform cleanup
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Done!");
	}
}
