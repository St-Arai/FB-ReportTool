package FindBugsManager.UI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class LoginPage implements ActionListener {

	private JFrame _frame = new JFrame();
	private JPanel panel = new JPanel();

	public LoginPage(JFrame frame) {
		_frame = frame;

		_frame.setSize(new Dimension(1000, 750));
		_frame.setTitle("");
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JButton button = new JButton("Login");
		JButton button2 = new JButton("Exit");

		button.setActionCommand("1");
		button2.setActionCommand("2");
		button.addActionListener(this);
		button2.addActionListener(this);

		panel.add(button);
		panel.add(button2);

		_frame.add(panel, BorderLayout.CENTER);

		_frame.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		int command = Integer.parseInt(action);

		switch (command) {
			case 1 :
				_frame.getContentPane().removeAll();
				new GitScanning(_frame);
				break;
			case 2 :
				_frame.dispose();
				_frame.setVisible(false);
				break;
			default :
				System.out.println("default.");
				break;
		}
	}
}
