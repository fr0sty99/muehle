package view;

import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import constants.AppColors;

/**
 * This class is responsible for showing the player who's turn it is and what
 * they should do next, if they dont know.
 * 
 * @author Joris
 */
class MessageView extends JPanel {
	private static final long serialVersionUID = -3951617955078779145L;
	private JLabel messageLabel;

	MessageView(int viewWidth, int viewHeight) {
		Dimension messagePanelDimension = new Dimension(viewWidth, viewHeight);
		setPreferredSize(messagePanelDimension);
		setMaximumSize(messagePanelDimension);

		setBackground(AppColors.messagePanel);
		setLayout(new GridBagLayout());

		messageLabel = new JLabel("message");
		add(messageLabel);
	}

	/**
	 * sets the message
	 * 
	 * @param message
	 *            the message to show
	 */
	public void setMessage(String message) {
		messageLabel.setText(message);
	}
	
	/**
	 * appends something to the current message
	 * 
	 * @param message
	 *            the message to show
	 */
	public void appendMessage(String message) {
		messageLabel.setText(messageLabel.getText() + " " + message);
	}
}
