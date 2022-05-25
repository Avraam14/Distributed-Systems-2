import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import serverpackage.Message;
import serverpackage.Notice;
import serverpackage.User;

public class ClientDelNotice {															//Class that creates a "delete notice" request
	public ClientDelNotice(NoticeBoard nboard, User user) {								//Constructor, we'll need both the main frame and the user
		JFrame delframe = new JFrame("Delete a Notice");								//Main frame
		JPanel contentPane = new JPanel();												//Panel for all the components
		JTextArea instructions = new JTextArea("To delete a notice type "				//Instructions for the user
				+ "the text of the notice and if you're the author we will "
				+ "delete it. Feel free to search for the notice before "
				+ "you delete it, so that the information is correct.");
		JTextField noticetext = new JTextField();										//We need to find the notice by its content
		JButton done = new JButton("Done");												//Ready button
		noticetext.setToolTipText("The notice text goes here");		
		
		done.addActionListener(ActionEvent ->{										
			if(noticetext.getText().equals("")) {										//We don't let the user send an empty notice
				JOptionPane.showMessageDialog(delframe, "You must fill in the "
						+ "field.", "Invalid Input", JOptionPane.INFORMATION_MESSAGE);
			}
			else {																		//Create the notice to be deleted
				Notice toboard = new Notice(noticetext.getText(), user);
				nboard.toBoard(new Message("DELETE", toboard, user));					//Send it over
				delframe.dispose();														//We don't need this
			}
		});
		
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));			//Packing everything
		delframe.setContentPane(contentPane);
		contentPane.add(instructions);
		contentPane.add(noticetext);
		contentPane.add(done);
		delframe.pack();
		delframe.setLocationRelativeTo(null);
		delframe.setVisible(true);
	}
}