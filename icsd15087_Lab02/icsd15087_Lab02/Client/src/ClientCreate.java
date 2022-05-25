//Avraam Katsigras 321/2015087 

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import serverpackage.Message;
import serverpackage.Notice;
import serverpackage.User;

public class ClientCreate {																//Class that creates a "create notice" request
	
	public ClientCreate(NoticeBoard nboard, User author) {								//Constructor, we'll need both the main frame and the user
		JFrame noticeframe = new JFrame("Create Notice");								//Main frame
		JPanel contentPane = new JPanel();												//Panel for all the components
		
		JTextField content = new JTextField();											//The notice text
		content.setToolTipText("Notice text goes here");
		
		JButton done = new JButton("Done");												//Ready button
		done.addActionListener(ActionEvent ->{
			if(content.getText().equals("")) {											//Check if text is empty
				JOptionPane.showMessageDialog(noticeframe, "You must fill in the field.", 
						"Invalid Input", JOptionPane.INFORMATION_MESSAGE);				//We don't let that happen
			} else {
				Notice toPost = new Notice(content.getText(), author);					//We create a new Notice
				nboard.toBoard(new Message("CREATE", toPost, author));					//And we send it over to the main GUI
				noticeframe.dispose();													//We don't need this anymore
			}
		});
		
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));			//Packing everything
		noticeframe.setContentPane(contentPane);
		contentPane.add(content);
		contentPane.add(done);
		noticeframe.pack();
		noticeframe.setLocationRelativeTo(null);
		noticeframe.setVisible(true);
	}
}
