package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

// This class is responsible for creating and showing the Window in which the game is playing
public class BoardView {
	public int screenWidth = 500;
	
	public void createAndShowFrame() {
		JFrame frame = new JFrame("NineManMorris");
				
				// What happens when the frame closes?
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				// set size of the frame
				Dimension d = new Dimension(screenWidth, screenWidth+200);
				frame.setSize(d);
				frame.setPreferredSize(d);
				frame.setMinimumSize(d);
				frame.setMaximumSize(d);
				frame.setResizable(true);
				
				// set Frame title
				frame.setTitle("Nine Man Morris");
				
				// set layout of the frame
				BorderLayout layout = new BorderLayout();
				frame.setLayout(layout);
				
	// create components and put them in Frame
				// add messagePanel, which is responsible for showing messages about the score and advises
				JPanel messagePanel = new JPanel();
				Dimension messagePanelDimension = new Dimension(screenWidth, 80);
				messagePanel.setPreferredSize(messagePanelDimension);
				messagePanel.setMaximumSize(messagePanelDimension);
				frame.add(messagePanel, BorderLayout.PAGE_START);

				// add boardPanel, which is responsible for showing the playground/board
				JPanel boardPanel = new JPanel();
				Dimension boardPanelDimension = new Dimension(screenWidth, 500);
				boardPanel.setPreferredSize(boardPanelDimension);
				boardPanel.setMaximumSize(boardPanelDimension);
				frame.add(boardPanel, BorderLayout.CENTER);
				
				// add playerPanel, which is responsible for showing the playernames and pieces to set, in the specific player color.
				JPanel playerPanel = new JPanel();
				playerPanel.setBackground(Color.GREEN);
				Dimension playerPanelDimension = new Dimension(screenWidth, 120);
				playerPanel.setPreferredSize(playerPanelDimension);
				playerPanel.setMaximumSize(playerPanelDimension);
				frame.add(playerPanel, BorderLayout.PAGE_END);
				
				// Size the frame			
				frame.pack();
				
				// set Location in Screen
				frame.setLocationRelativeTo(null);
				
				// Show frame
				frame.setVisible(true);
			}
}
