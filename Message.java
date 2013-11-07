import java.io.*;
import java.util.*;

public class Message implements Serializable {
	private String from = null;
	private String recipient = null;
	private String data = "";

	private static String STORAGE_FILENAME = "messages.dat";
	private static File STORAGE = new File(STORAGE_FILENAME);

	public Message(String f, String r) {
		from = f;
		recipient = r;
	}

	public void writeln(String str) {
		data += str + "\n";
	}

	public String toString() {
		return "To: " + recipient + "\nFrom: "  + from + "\n" + data;
	}

	public static List<Message> readFromFile() {
		ObjectInputStream ois = null;
		List<Message> all = null;
		try {
			all = new ArrayList<Message>();
			ois = new ObjectInputStream(new FileInputStream(STORAGE));
			while (true) {
				try {
					all.add((Message)ois.readObject());
				} catch (EOFException e) {
					break;
				}
			}
		} catch (IOException e) {
			System.out.println("Error reading from messages.dat");
			return null;
		} catch (ClassNotFoundException e) {
			System.out.println("Couldn't find class Message");
		}
		return all;
	}

	public static boolean writeToFile(List<Message> all) {
		ObjectOutputStream ous = null;
		try {
			ous = new ObjectOutputStream(new FileOutputStream(STORAGE));
			for (Message m : all) {
				ous.writeObject(m);
			}
		} catch (FileNotFoundException e) {
			try {
				if (STORAGE.createNewFile()) {
					System.out.println("File created!");
				}
			} catch (IOException ie) {
				ie.printStackTrace();
			}
			System.out.println("Storing message failed because storage file did not exist");
			return false;
		} catch (IOException e) {
			System.out.println("Error while writing to storage file");
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (ous != null)
					ous.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	public static List<Message> load(String user) {
		List<Message> all = readFromFile();
		List<Message> forUser = new ArrayList<Message>();
		if (all == null) return forUser;
		for (Message m : all) {
			if (m.recipient.equals(user)) {
				forUser.add(m);
			}
		}
		for (Message m : forUser) {
			all.remove(m);
		}
		writeToFile(all);
		return forUser;
	}

	public boolean store() {
		List<Message> all = readFromFile();
		if (all == null) all = new ArrayList<Message>();
		all.add(this);
		return writeToFile(all);
	}
}
