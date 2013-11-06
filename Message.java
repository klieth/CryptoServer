import java.io.*;
import java.util.*;

public class Message implements Serializable {
	private String recipient = null;
	private String data = "";

	private static String STORAGE_FILENAME = "messages.dat";
	private static File STORAGE = new File(STORAGE_FILENAME);

	public Message(String r) {
		recipient = r;
	}

	public void writeln(String str) {
		data += str + "\n";
	}

	public static Message load(String user) {
	}

	public boolean store() {
		ObjectInputStream ois = null;
		List<Message> all = null;;
		try {
			all = new ArrayList<Message>();
			ois = new ObjectInputStream(new FileInputStream(STORAGE));
			while (ois.available() > 0) {
				all.add((Message)ois.readObject());
			}
			all.add(this);
		} catch (IOException e) {
			System.out.println("Error while reading from storage file");
		} catch (ClassNotFoundException e) {
			System.out.println("Couldn't find Message class");
			return false;
		} finally  {
			try {
				if (ois != null) {
					ois.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (all == null) {
				System.out.println("Storing message failed because old messages could nt be loaded");
				return false;
			}
		}
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
}
