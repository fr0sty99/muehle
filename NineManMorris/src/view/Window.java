package view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Window {
	public final int screenWidth = 500;
	
	public void createWindow() {
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
		
		frame.add(new MessageView(screenWidth, 80).getMessageView(), BorderLayout.PAGE_START);
		frame.add(new BoardView(screenWidth,  screenWidth).getBoardView(), BorderLayout.CENTER);
		frame.add(new PlayerView(screenWidth, 120).getPlayerView(), BorderLayout.PAGE_END);
		
		// Size the frame			
		frame.pack();
		
		// set Location in Screen
		frame.setLocationRelativeTo(null);
		
		// Show frame
		frame.setVisible(true);
		// END WIP
	}
}
