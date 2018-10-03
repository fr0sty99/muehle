package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

import model.Board;

/**
 * This class is adding the UI together and represents the Window/View of this program
 * @author Joris
 *
 */
public class MyView implements Observer {
	private final int screenWidth = 500;
	private JFrame frame;

	// public so we can access our data and UI elements
	public MessageView messageView;
	public GameView gameView;

	public MyView() {
		createWindow();
	}

	/**
	 * creates our frame and adds the Components to it
	 */
	public void createWindow() {
		createComponents();

		frame = new JFrame("NineManMorris");

		// What happens when the frame closes?
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// set size of the frame
		Dimension d = new Dimension(screenWidth, screenWidth + 140);
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

		// Size the frame
		frame.pack();

		// set Location in Screen
		frame.setLocationRelativeTo(null);

		// Show frame
		frame.setVisible(true);
	}
	
	/**
	 * adds a mouseListener to our contentPane
	 * @param listener the MouseListener to add
	 */
	public void addMyMouseListener(MouseListener listener) {
		frame.getContentPane().addMouseListener(listener);
	}

	/** 
	 * creates the components used for this frame
	 */
	public void createComponents() {
		messageView = new MessageView(screenWidth, 80);
		gameView = new GameView(screenWidth, screenWidth);
	}

	/** 
	 *  gets called when the model calls notifyObserver. for updating the GUI
	 */
	@Override
	public void update(Observable o, Object arg) {
		gameView.drawPiecesOnPlayerPanel(((Board) arg).getPlayers());
		gameView.drawGridWithPieces(((Board) arg).getNodeSets());
	}

}
