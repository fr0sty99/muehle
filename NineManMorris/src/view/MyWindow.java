package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MyWindow {
	public final int screenWidth = 500;
	public MessageView messageView;
	public BoardView boardView;
	public PlayerView playerView;
	
	public MyWindow() {
		createWindow();
	}

	public void createComponents() {
		messageView = new MessageView(screenWidth, 80);
		boardView = new BoardView(screenWidth, screenWidth);
		playerView = new PlayerView(screenWidth, 120);
	}

	public void createWindow() {
		createComponents();
		playerView.setPlayerLabels();
		
		JFrame frame = new JFrame("NineManMorris");

		// What happens when the frame closes?
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// set size of the frame
		Dimension d = new Dimension(screenWidth, screenWidth + 220);
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

		// add Components to my JFrame
		frame.add(messageView, BorderLayout.PAGE_START);
		frame.add(boardView, BorderLayout.CENTER);
		frame.add(playerView, BorderLayout.PAGE_END);

		// Size the frame
		frame.pack();

		// set Location in Screen
		frame.setLocationRelativeTo(null);

		// Show frame
		frame.setVisible(true);
		// END WIP
	}

}
