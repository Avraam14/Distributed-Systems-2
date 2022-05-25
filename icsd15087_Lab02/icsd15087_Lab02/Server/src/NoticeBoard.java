//Avraam Katsigras 321/2015087

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;

import serverpackage.Message;
import serverpackage.Notice;
import serverpackage.User;

public class NoticeBoard {												//This class manages the messages and the files
	private final String userfile = "users.txt";						//Users file
	private final String noticefile = "notices.txt";					//Notices file
	private ArrayList<User> users;										//All the users
	private ArrayList<Notice> notices;									//All the notices
	
	public NoticeBoard() {												//Constructor
		users = new ArrayList<User>();
		notices = new ArrayList<Notice>();
		try (ObjectInputStream filein =									//Fill in the ArrayLists with objects from the files 
				new ObjectInputStream(new FileInputStream(userfile));){
			while(true)
				users.add((User) filein.readObject());
		} catch (FileNotFoundException e) {
			System.out.println("Users file was not found");
		} catch (EOFException e) {			
		} catch (ClassNotFoundException e) {
			System.out.println("Bad data written in the files");		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try (ObjectInputStream filein = 
				new ObjectInputStream(new FileInputStream(noticefile));){
			while(true)
				notices.add((Notice) filein.readObject());
		} catch (FileNotFoundException e) {
			System.out.println("Notices file was not found");
		} catch (EOFException e) {
		} catch (ClassNotFoundException e) {
			System.out.println("Bad data written in the files");		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")										//We know that everything goes by protocol. No need to check unpacking
	Message respond(Message clientmsg) {								//This method creates the server's response to the client
		Message servermsg = null;										//Nullifies the message to avoid sending the same one again
		String msg = clientmsg.message();								//Client's message
		if(msg.equals("CONNECTION")) {									//If client requests connection we let him know that he is connected
			servermsg = new Message("CONNECTED");
		} else if (msg.equals("TERMINATE")) {							//If client requests termination we save everything and go on
			servermsg = new Message("TERMINATED");
			saveAll();		
		} else if (msg.equals("SIGNUP")) {								//Client signs up in the user file
			ArrayList<String> credentials = (ArrayList<String>)
					clientmsg.content();								//We get the user name and password
			User newuser = new User(credentials.get(0), 				//Create a new user by the credentials provided
					credentials.get(1));
			users.add(newuser);											//We add the user
			servermsg = new Message("SUCCESS");							//Everything went smoothly
			saveAll();													//Save everything in case of unexpected crash
		} else if (msg.equals("SIGNIN")) {								//Client wants to sign in
			ArrayList<String> credentials = (ArrayList<String>) 		//We get the credentials
					clientmsg.content();
			User toauth = new User(credentials.get(0),					//And we check if it correspond to any user in the file 
					credentials.get(1));
			if(users.contains(toauth)) {								//If the client's authenticated we let him know
				servermsg = new Message("USER", toauth, null);			//Otherwise we let him know
				saveAll();
			}
			else
				servermsg = new Message("NOAUTH");
		} else if (msg.equals("CREATE")) {								//Client creates a notice
			notices.add((Notice) clientmsg.content());					//We simply get the notice and write it to the file
			servermsg = new Message("SUCCESS");							//We let him know it's done
			saveAll();
		} else if (msg.equals("SEARCH")) {								//Client wants to see some notices
			ArrayList<LocalDate> dates = (ArrayList<LocalDate>)			//We get the dates
					clientmsg.content();
			servermsg = new Message("NOTICES", 							//We return all the notices found
					getNotices(dates.get(0), dates.get(1)), null);
			saveAll();
		} else if (msg.equals("DELETE")) {								//Client deletes a notice
			Notice todel = (Notice) clientmsg.content();				//We get the notice to be deleted
			servermsg = deleteNotice(todel, clientmsg.user());			//And we follow procedure letting him know how it goes
			saveAll();
		} else if (msg.equals("MODIFY")) {								//Client modifies a notice, everything's the same as above
			ArrayList<Notice> notices = (ArrayList<Notice>) 
					clientmsg.content();
			servermsg = modifyNotice(notices, clientmsg.user());
			saveAll();
		}
		return servermsg;												//return null
	}
	
	private Message modifyNotice(ArrayList<Notice> notices2, User user) {
		for(Notice infile : notices) {									//We get all the notices
			if(infile.equals(notices2.get(0))) {						//Check if there's any notice like the one
				if(infile.checkUser(user)) {							//The client gave us. And if he's the author
					notices.remove(infile);								//We replace it in the file
					notices.add(notices2.get(1));
					return new Message("SUCCESS");						//That went well
				}
				else
					return new Message("NOAUTH");						//If the user's not the author we let him know
			}
		}
		return new Message("NOTFOUND");									//Nothing of the sort found
	}

	private Message deleteNotice(Notice todel, User user) {				
		for(Notice infile : notices) {									//We get all the notices
			if(infile.equals(todel)) {									//Check if there's any notice like the one
				if(infile.checkUser(user)) {							//The client gave us. And if he's the author
					notices.remove(infile);								//We remove it from in the file
					return new Message("SUCCESS");						//That went smoothly
				}
				else
					return new Message("NOAUTH");						//If the user's not the author we let him know
			}
		}
		return new Message("NOTFOUND");									//Nothing of the sort found
	}

	private ArrayList<Notice> getNotices(LocalDate from, LocalDate to){	
		ArrayList<Notice> toreturn = new ArrayList<Notice>();			//We get all the notices in the time frame
		for(Notice infile : notices) {									//Not really complicated
			if(infile.checkDates(from, to)) {
				toreturn.add(infile);
			}
		}
		return toreturn;
	}
	
	void saveAll() {													//Save everything to the files
		try(
				ObjectOutputStream userout = 							//Overwrite the file with the new info
					new ObjectOutputStream(new FileOutputStream(userfile));
				ObjectOutputStream noticeout = 
					new ObjectOutputStream(new FileOutputStream(noticefile));
				){
			for(User inlist : users)
				userout.writeObject(inlist);
			for(Notice inlist : notices)
				noticeout.writeObject(inlist);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
}