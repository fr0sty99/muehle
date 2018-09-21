package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

// The MessageView is responsible for informing the players who's turn it is and what they should do next, if they dont know.
public class MessageView extends JPanel{
	private JLabel messageLabel;

	public int viewWidth; // should be windowd width
	public int viewHeight; // should be like 80

	public MessageView(int viewWidth, int viewHeight) {
		this.viewWidth = viewWidth;
		this.viewHeight = viewHeight;


		// set size
		Dimension messagePanelDimension = new Dimension(viewWidth, viewHeight);
		setPreferredSize(messagePanelDimension);
		setMaximumSize(messagePanelDimension);

		setBackground(Color.red);

		// set Layout
		setLayout(new GridBagLayout());

		// add Label
		messageLabel = new JLabel("message");
		add(messageLabel);
	}

	public void setMessage(String message) {
		messageLabel.setText(message);
	}

}
