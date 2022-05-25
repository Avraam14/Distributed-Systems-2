//Avraam Katsigras 321/2015087

import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import serverpackage.Notice;

public class ClientPrintNotices {													//Class that displays all the notices (after requested)
	public ClientPrintNotices(ArrayList<Notice> toprint) {							
		JFrame printframe = new JFrame("Notices");
		JTextArea[] notices = new JTextArea[toprint.size()];
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		printframe.setContentPane(contentPane);
		
		if(toprint.isEmpty()) {														//If we don't get any notices we display the message
			JOptionPane.showMessageDialog(printframe, "No notices found in these dates.",
					"No Notices Found", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		for(int i = 0; i < notices.length; i++) {
			notices[i] = new JTextArea(toprint.get(i).toString());
			contentPane.add(notices[i]);
		}
		
		printframe.pack();
		printframe.setLocationRelativeTo(null);
		printframe.setVisible(true);
	}
}
