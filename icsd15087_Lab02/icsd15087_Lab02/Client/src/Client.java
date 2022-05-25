//Avraam Katsigras 321/2015087

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import serverpackage.Message;

public class Client {															//Main class everything about the client happens here

	public static void main(String[] args) {
		try (																	//Try-with-resources so that resources can be closed properly
				Socket sock = new Socket("localhost", 5555);					//The client's socket that will connect to the server
				ObjectOutputStream objout = new ObjectOutputStream(				//Stream for writing objects - sending messages
						sock.getOutputStream());			
				ObjectInputStream objin = new ObjectInputStream(				//Stream for reading objects - receiving messages
						sock.getInputStream());
				) {
			
			Message clientmsg = new Message("CONNECTION");						//Request connection from the server
			objout.writeObject(clientmsg);
			
			Message servermsg = (Message) objin.readObject();					//Reading the message from the server
			if(servermsg.message().equals("CONNECTED")) {						//If it's according to the protocol we proceed
				NoticeBoard nboard = new NoticeBoard();							//User's GUI: for the user it's all happening here
				do {
					System.out.println("Waiting for message...");				
					do {
						System.out.print("");									//For some reason this helps us create a message
						clientmsg = nboard.toSend();							//Get the message from the GUI
					}while(clientmsg == null);									//We don't want to send an empty message
					objout.writeObject(clientmsg);								//Send the message
					servermsg = null;											//Reset the server's message
					do {
						servermsg = (Message) objin.readObject();				//Read the server's response
					}while(servermsg == null);									//We don't want an empty response
					nboard.receive(servermsg);									//The GUI will figure out what to do with the message
				}while(!servermsg.message().equals("TERMINATED"));				//Until the connection is terminated by user's request
			}
			System.out.println("Closing application...");
		} catch (IOException e) {
			System.out.println("There was a problem connecting to the server");
		} catch (ClassNotFoundException e) {
			System.out.println("Couldn't read response from the server");
		}
	}

}
