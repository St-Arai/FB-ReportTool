package FindBugsManager.UI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Battle implements ActionListener {

	private JFrame _frame = new JFrame();
	private int _statusPoint = 0;

	private JPanel statusPanel = new JPanel();
	private JPanel panel = new JPanel();
	private JPanel panel2 = new JPanel();
	private JPanel panel3 = new JPanel();
	private JPanel panel4 = new JPanel();

	private JLabel label = new JLabel();
	private JLabel atkLabel = new JLabel();
	private JLabel iconLabel = new JLabel();

	private JButton plusButton = new JButton("+");
	private JButton minusButton = new JButton("-");

	private JButton battleButton = new JButton("Battle!");

	private int atk = 0;

	public Battle(JFrame mainFrame, final int sum) {
		_frame = mainFrame;

		_frame.setSize(new Dimension(750, 500));
		_frame.setTitle("");
		_frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

		_statusPoint = sum;

		plusButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!(_statusPoint <= 0)) {
					atk++;
					_statusPoint--;
					label.setText("points : " + _statusPoint);
					atkLabel.setText("ATK : " + atk);
				}
			}
		});

		minusButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!(_statusPoint >= sum)) {
					atk--;
					_statusPoint++;
					label.setText("points : " + _statusPoint);
					atkLabel.setText("ATK : " + atk);
				}
			}
		});

		battleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ImageIcon icon = null;
				if (atk < 10) {
					icon = new ImageIcon("./img/defeat.jpg");
				} else {
					icon = new ImageIcon("./img/win.jpg");
				}
				iconLabel.setIcon(icon);
			}
		});

		panel4.add(iconLabel);
		panel3.add(battleButton);

		label.setText("points : " + _statusPoint);
		panel2.add(label);

		atkLabel.setText("ATK : " + atk);
		statusPanel.add(atkLabel);
		statusPanel.add(plusButton);
		statusPanel.add(minusButton);

		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.add(panel2);
		panel.add(statusPanel);
		panel.add(panel3);
		panel.add(panel4);

		_frame.add(panel, BorderLayout.CENTER);
		_frame.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
	}
}