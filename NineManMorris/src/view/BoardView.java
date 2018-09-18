package view;

import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JPanel;

import assets.AppColors;

// This class is responsible for creating and showing the Window in which the game is playing
public class BoardView {
	private JPanel panel;
	private Canvas canvas; // here we are gonna draw our game

	public BoardView(int viewWidth, int viewHeight) {
		// create components and put them in Frame

		// add boardPanel, which is responsible for showing the playground/board
		panel = new JPanel();
		Dimension boardPanelDimension = new Dimension(viewWidth, viewHeight);
		panel.setBackground(AppColors.panelDefaultColor);
		panel.setPreferredSize(boardPanelDimension);
		panel.setMaximumSize(boardPanelDimension);

	}

	public JPanel getBoardView() {
		return panel;
	}
}
