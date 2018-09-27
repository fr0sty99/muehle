package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

import model.BoardModel;
import model.NodeSet;
import model.Player;

public class MyView implements Observer {
	public final int screenWidth = 500;
	public MessageView messageView;
	public GameView gameView;
	private JFrame frame;
	// public PlayerView playerView; / add playerView to gameView

	public MyView() {
		createWindow();
	}

	public void addMyMouseListener(MouseListener listener) {
		frame.getContentPane().addMouseListener(listener);
	}

	public void createComponents() {
		messageView = new MessageView(screenWidth, 80);
		gameView = new GameView(screenWidth, screenWidth);
		// playerView = new PlayerView(screenWidth, 120); // add playerView to
		// gameView
	}

	public void createWindow() {
		createComponents();

		frame = new JFrame("NineManMorris");

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
		frame.getContentPane().add(messageView, BorderLayout.PAGE_START);
		frame.getContentPane().add(gameView, BorderLayout.CENTER);

		// frame.add(playerView, BorderLayout.PAGE_END); move to gameView

		// Size the frame
		frame.pack();

		// set Location in Screen
		frame.setLocationRelativeTo(null);

		// Show frame
		frame.setVisible(true);
		// END WIP
	}

	@Override
	public void update(Observable o, Object arg) {
		// who called us and what did they send?
		System.out.println("View      : Observable is " + o.getClass() + ", object passed is " + arg.getClass());
		gameView.drawPiecesOnPlayerPanel(((BoardModel) arg).getPlayers());
		gameView.drawGridWithPieces(((BoardModel) arg).getNodeSets());
	}

}
