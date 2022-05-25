//Avraam Katsigras 321/2015087

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import serverpackage.Message;

public class Server {

	public static void main(String[] args) {
		try {
			ObjectInputStream objin;
			ObjectOutputStream objout;
			Message clientmsg, servermsg;
			while (true){
				ServerSocket server = new ServerSocket(5555, 1);
				System.out.println("Accepting Connection...");
				System.out.println("Local Address :" + server.getInetAddress() + 
						" Port :" + server.getLocalPort());
				
				Socket sock = server.accept();
				try {
					objout = new ObjectOutputStream(sock.getOutputStream());
					objin = new ObjectInputStream(sock.getInputStream());
					
					clientmsg = (Message) objin.readObject();
					if(clientmsg.message().equals("CONNECTION")) {
						NoticeBoard nboard = new NoticeBoard();
						do {
							servermsg = null;
							servermsg = nboard.respond(clientmsg);
							System.out.println(clientmsg.message());
							objout.writeObject(servermsg);
							do {
								clientmsg = (Message) objin.readObject();
							}while(clientmsg == null);
						}while(!clientmsg.message().equals("TERMINATE"));
					}
				}catch(SocketException ex) {
					System.out.println("Client disconnected.");
				}catch(EOFException ex) {
					System.out.println("Client couldn't connect.");
				}
				System.out.println("Closing connection...");
				sock.close();
				server.close();
			}
		}catch(ClassNotFoundException | IOException ex) {
			ex.printStackTrace();
		}
	}
}