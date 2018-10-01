package view;

import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import constants.AppColors;

// The MessageView is responsible for informing the players who's turn it is and what they should do next, if they dont know.
public class MessageView extends JPanel{
	private static final long serialVersionUID = -3951617955078779145L;
	private JLabel messageLabel;
	public int viewWidth;
	public int viewHeight;

	public MessageView(int viewWidth, int viewHeight) {
		this.viewWidth = viewWidth;
		this.viewHeight = viewHeight;

		Dimension messagePanelDimension = new Dimension(viewWidth, viewHeight);
		setPreferredSize(messagePanelDimension);
		setMaximumSize(messagePanelDimension);
		
		setBackground(AppColors.messagePanel);
		setLayout(new GridBagLayout());

		messageLabel = new JLabel("message");
		add(messageLabel);
	}

	public void setMessage(String message) {
		messageLabel.setText(message);
	}

}
