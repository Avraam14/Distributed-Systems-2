//Avraam Katsigras 321/2015087

import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import serverpackage.Message;

public class ClientSignUp{												//Class that sends a "Create Account" request
	public ClientSignUp(NoticeBoard nboard) {
		JFrame signframe = new JFrame("Sign Up");
		JPanel contentPane = new JPanel();
		JTextField uname = new JTextField();							//Username and password
		JTextField pass = new JTextField();
		JButton done = new JButton("Done");
		uname.setToolTipText("Username goes here");
		pass.setToolTipText("Password goes here");
		done.addActionListener(ActionEvent ->{
			if(uname.getText().equals("") || pass.getText().equals("")) {
				JOptionPane.showMessageDialog(signframe, "You must fill in the fields.", 
						"Invalid Input", JOptionPane.INFORMATION_MESSAGE);
			} else {
				ArrayList<String> content = new ArrayList<String>();
				content.add(uname.getText());
				content.add(pass.getText());
				nboard.toBoard(new Message("SIGNUP", content, null));
				signframe.dispose();
			}
		});
		
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		signframe.setContentPane(contentPane);
		contentPane.add(uname);
		contentPane.add(pass);
		contentPane.add(done);
		signframe.pack();
		signframe.setLocationRelativeTo(null);
		signframe.setVisible(true);
	}
}
