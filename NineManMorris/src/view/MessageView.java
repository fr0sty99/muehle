package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

// The MessageView is responsible for informing the players who's turn it is and what they should do next, if they dont know.
public class MessageView {
	private JPanel panel;
	private JLabel messageLabel;

	public int viewWidth; // should be windowd width
	public int viewHeight; // should be like 80

	public MessageView(int viewWidth, int viewHeight) {
		this.viewWidth = viewWidth;
		this.viewHeight = viewHeight;

		// create new JPanel and add a Label to it, where we can display the messages
				panel = new JPanel();
				
				// set size
				Dimension messagePanelDimension = new Dimension(viewWidth, viewHeight);
				panel.setPreferredSize(messagePanelDimension);
				panel.setMaximumSize(messagePanelDimension);
				
				panel.setBackground(Color.red);
				
				// set Layout
				panel.setLayout(new GridBagLayout());

				// add Label 
				messageLabel = new JLabel("message");
				panel.add(messageLabel);			
	}
	
	public JPanel getMessageView() {
		return panel;
	}
	
	

}
