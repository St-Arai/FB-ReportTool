package FindBugsManager.UI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class LoginPage extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel panel = new JPanel();

	public LoginPage() {
		setSize(new Dimension(1000, 750));
		setTitle("");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JButton button = new JButton("Login");
		JButton button2 = new JButton("Exit");

		button.setActionCommand("1");
		button2.setActionCommand("2");
		button.addActionListener(this);
		button2.addActionListener(this);

		panel.add(button);
		panel.add(button2);
		add(panel, BorderLayout.CENTER);

		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		int command = Integer.parseInt(action);

		switch (command) {
			case 1 :
				getContentPane().removeAll();
				new GitScanning(this);
				break;
			case 2 :
				dispose();
				setVisible(false);
				break;
			default :
				System.out.println("default.");
				break;
		}
	}
}
