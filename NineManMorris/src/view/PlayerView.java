package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import javax.swing.JLabel;
import javax.swing.JSplitPane;

import constants.AppColors;
import constants.Owner;
import model.Player;

public class PlayerView extends JSplitPane {
	public JLabel playerOneLabel;
	public JLabel playerTwoLabel;

	// width should be windowd width and height should be like 120
	public PlayerView(int viewWidth, int viewHeight) {
		// add playerPanel, which is responsible for showing the playernames and
		// pieces to set, in the specific player color.
		setBackground(Color.GREEN);
		Dimension playerPanelDimension = new Dimension(viewWidth, viewHeight);
		setPreferredSize(playerPanelDimension);
		setMaximumSize(playerPanelDimension);

		setBackground(AppColors.panelDefaultColor);

		// split our splitPane
		setDividerSize(0);
		setDividerLocation(viewHeight / 2);
	}

	public void setTopLabel(String text) {
		playerOneLabel.setText(text);
	}
	
	public void setBottomLabel(String text) {
		playerTwoLabel.setText(text);
	}
	
	public void drawCoins(int numCoins, Owner owner) {
		Color fill;
		int offSet = 0;
		if (owner == Owner.PLAYER1) {
			fill = Color.WHITE;
			offSet = 50;
		} else {
			fill = Color.BLACK;
			offSet = 100;
		}
		Graphics2D g = (Graphics2D) getGraphics();
		g.setColor(fill);
		
		int x = 0, y = 0;
		int width = 10, height = 10;
		
		for(int i = 0; i < numCoins; i++) {
			g.fillOval(x , y + offSet, width, height);
			x += 10;
		}
		
	}
	
	public void setPlayerLabels(/*Player[] players*/) {
		// todo: set player labels dynamically

		// create the labels for our component

		playerOneLabel = new JLabel("Player 1");
		playerOneLabel.setForeground(AppColors.playerOneColor);

		playerTwoLabel = new JLabel("Player 2");
		playerTwoLabel.setForeground(AppColors.playerTwoColor);

		// set orientation and components of our splitPane
		setOrientation(JSplitPane.VERTICAL_SPLIT);
		setTopComponent(playerOneLabel);
		setBottomComponent(playerTwoLabel);
	}

}
