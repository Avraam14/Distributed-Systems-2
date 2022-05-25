//Avraam Katsigras 321/2015087

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import serverpackage.Message;

public class ClientSearch {														//Class that sends the "display notices" request

	public ClientSearch(NoticeBoard nboard) {									
		JFrame searchframe = new JFrame("Sign Up");						
		JPanel contentPane = new JPanel();
		JTextField from = new JTextField();
		JTextField to = new JTextField();
		JButton done = new JButton("Done");
		from.setToolTipText("from (yyyy-MM-dd)");								//The user needs to put the date in a certain way
		to.setToolTipText("to (yyyy-MM-dd)");
		done.addActionListener(ActionEvent ->{
			try {
				LocalDate fromdate = LocalDate.parse(from.getText());			//Try and parse the text the user wrote if it doesn't
				LocalDate todate = LocalDate.parse(to.getText());				//we just display a message
				
				ArrayList<LocalDate> tosend = new ArrayList<LocalDate>();
				tosend.add(fromdate);
				tosend.add(todate);
				nboard.toBoard(new Message("SEARCH", tosend, null));			//Send the message over
				searchframe.dispose();
			} catch(DateTimeParseException ex) {
				JOptionPane.showMessageDialog(searchframe, 
						"You must fill in the fields with valid Dates", 
						"Invalid Input", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));	//Pack everything
		searchframe.setContentPane(contentPane);
		contentPane.add(from);
		contentPane.add(to);
		contentPane.add(done);
		searchframe.pack();
		searchframe.setLocationRelativeTo(null);
		searchframe.setVisible(true);
	}

}
