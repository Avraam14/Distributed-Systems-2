//Avraam Katsigras 321/2015087

import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import serverpackage.Message;
import serverpackage.Notice;
import serverpackage.User;

public class NoticeBoard {												//Class that manages everything user-side related
	private Message toserver;											//The message that will be sent to the server
	private JFrame mainframe;											//Main frame
	private User user = null;											//The current user
	public NoticeBoard() {												//Main constructor
		toserver = null;												//No message just yet
		mainframe = new JFrame();
		JButton signup = new JButton("Sign Up");						//All the buttons
		JButton signin = new JButton("Sign In");
		JButton create = new JButton("Create Notice");
		JButton search = new JButton("Display Notices");
		JButton delete = new JButton("Delete a Notice");
		JButton modify = new JButton("Modify a Notice");
		
		signup.addActionListener(ActionEvent ->{						//The user creates a new account with ClientSignUp 
			new ClientSignUp(this);										
		});
		
		signin.addActionListener(ActionEvent ->{						//The user signs is through ClientSignIn
			if(user == null) {											//If there is no user already signed in
				new ClientSignIn(this);									//The user just sends a request
			}															//Else we check if user wants to sign out
			else{
				if(JOptionPane.showConfirmDialog(mainframe, "You are already"
						+ " signed in. Change account?", "Already Signed In",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					new ClientSignIn(this);
				}
			}
		});
		
		create.addActionListener(ActionEvent ->{						//If the user wants to create a new notice
			if(user == null) {											//We make sure he is signed in first
				JOptionPane.showMessageDialog(mainframe, "You need to be"
						+ " signed in for this action.", "Sign In Required",
						JOptionPane.INFORMATION_MESSAGE);
			}
			else														//And if he is we go through creating a notice
				new ClientCreate(this, user);
		});
		
		search.addActionListener(ActionEvent ->{						//User requests to see some notices
			new ClientSearch(this);
		});
		
		delete.addActionListener(ActionEvent ->{						//If the user wants to delete a notice
			if(user == null) {											//We make sure he is logged in first
				JOptionPane.showMessageDialog(mainframe, "You need to be"
						+ " signed in for this action.", "Sign In Required",
						JOptionPane.INFORMATION_MESSAGE);
			}
			else {														//And if he is we go through with deleting a notice
				new ClientDelNotice(this, user);
			}	
		});
		
		modify.addActionListener(ActionEvent ->{						//If the user wants to delete a notice
			if(user == null) {											//We make sure he is logged in first
				JOptionPane.showConfirmDialog(mainframe, "You need to be"
						+ " signed in for this action. Sign in?", "Sign In Required",
						JOptionPane.INFORMATION_MESSAGE);
			}															//And if he is we go through with it
			else {
				new ClientModNotice(this, user);
			}
		});
		
		mainframe.addWindowListener(new WindowAdapter(){				//When the user closes the window
            @Override
            public void windowClosing(WindowEvent e){
                toserver = new Message("TERMINATE");					//Request termination according to protocol
            }
        });
		mainframe.setLayout(new FlowLayout());							//Packing the buttons in line
		mainframe.add(signup);
		mainframe.add(signin);
		mainframe.add(create);
		mainframe.add(search);
		mainframe.add(delete);
		mainframe.add(modify);
		mainframe.pack();
		mainframe.setLocationRelativeTo(null);
		mainframe.setVisible(true);
		mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	Message toSend() {													//The Client class will pull the request to be sent
		Message toreturn = toserver;
		toserver = null;												//We send then nullify the message to avoid the same message
		return toreturn;												//Being sent twice
	}

	void toBoard(Message message) {										//This gets called by all the classes that create requests
		toserver = message;												
	}
	
	@SuppressWarnings("unchecked")										//This method manages all messages from the server 
	void receive(Message servermsg) {									//And displays them properly
		String msg = servermsg.message();								//The message code from the server
		if(msg.equals("SUCCESS")) {										//If something is successful we print so
			JOptionPane.showMessageDialog(mainframe, 
					"Operation completed successfully!", msg,
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		if(msg.equals("USER")) {										//This is how the server tells us that a user was authenticated
			user = (User) servermsg.content();							//We get the User image that the user provides
			JOptionPane.showMessageDialog(mainframe, 					//And we display the message		
					"User signed in successfully!", msg,
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		if(msg.equals("NOAUTH")) {										//This is how the server tells is that a user was not authenticated
			JOptionPane.showMessageDialog(mainframe, 					//We just let the user know
					"Wrong credentials, couldn't sign in.", msg,
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		if(msg.equals("NOTFOUND")) {									//This is how the server tells us that the notice wasn't found
			JOptionPane.showMessageDialog(mainframe,					//This is a response to MODIFY or DELETE
					"Couldn't find the notice.", msg,					//Display the message
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		if(msg.equals("NOTICES")) {										//This is how the server sends us the notices in a time frame
			ArrayList<Notice> toprint = (ArrayList<Notice>)
					servermsg.content();								//We display them all
			new ClientPrintNotices(toprint);
			return;
		}
		if(msg.equals("ERROR")) {										//The server encountered an error. Big trouble.
			JOptionPane.showMessageDialog(mainframe, 					//We let the user know that we can't write proper code
					"Server-side error.", msg,
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
	}
}