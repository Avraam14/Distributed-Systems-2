//Avraam Katsigras 321/2015087

import java.util.ArrayList;

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

public class ClientModNotice {														//This class is almost identical to ClientDelNotice
	public ClientModNotice(NoticeBoard nboard, User user) {							//Check that class's comments for explanations
		JFrame modframe = new JFrame("Modify a Notice");
		JPanel contentPane = new JPanel();
		JTextArea instructions = new JTextArea("To modify a notice type "
				+ "the old and new text of the notice and if you're the "
				+ "author we will modify it. Feel free to search for the "
				+ "notice before you modify it, so that he information is correct.");
		JTextField oldtext = new JTextField();
		JTextField newtext = new JTextField();
		JButton done = new JButton("Done");
		oldtext.setToolTipText("The old notice text goes here");
		newtext.setToolTipText("The new notice text goes here");
		
		done.addActionListener(ActionEvent ->{
			if(oldtext.getText().equals("") || newtext.getText().equals("")) {
				JOptionPane.showMessageDialog(modframe, "You must fill in the "
						+ "fields.", "Invalid Input", JOptionPane.INFORMATION_MESSAGE);
			}
			else {
				Notice oldnotice = new Notice(oldtext.getText(), user);
				Notice newnotice = new Notice(newtext.getText(), user);
				ArrayList<Notice> toboard = new ArrayList<Notice>();
				toboard.add(oldnotice);
				toboard.add(newnotice);
				nboard.toBoard(new Message("MODIFY", toboard, user));
				modframe.dispose();
			}
		});
		
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		modframe.setContentPane(contentPane);
		contentPane.add(instructions);
		contentPane.add(oldtext);
		contentPane.add(newtext);
		contentPane.add(done);
		modframe.pack();
		modframe.setLocationRelativeTo(null);
		modframe.setVisible(true);
	}
}
