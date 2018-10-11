package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

import model.NodeSet;
import model.Player;

/**
 * This class is adding the UI together and represents the Window/View of this
 * program
 * 
 * @author Joris
 *
 */
public class MyView extends Observable {
	private final int screenWidth = 500;
	private JFrame frame;
	private MyCanvas canvas;
	private MessageView messageView; // we need to acces our UI Elements

	public MyView(Observer observer) {
		createWindow();
		addObserver(observer);
	}

	/**
	 * creates our frame and adds the Components to it
	 */
	private void createWindow() {
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

		// set layout of the frame
		frame.setLayout(new BorderLayout());

		// add Components to my JFrame
		frame.getContentPane().add(canvas, BorderLayout.CENTER);
		frame.getContentPane().add(messageView, BorderLayout.PAGE_END);

		// Size the frame
		frame.pack();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setTitle("Muehle by Joris Neber");

		// Show frame
		frame.setVisible(true);
	}

	/**
	 * creates the components used for this frame
	 */
	private void createComponents() {
		// create messageView
		messageView = new MessageView(screenWidth, 80);

		// create myCanvas
		canvas = new MyCanvas();
		canvas.setPreferredSize(new Dimension(screenWidth, screenWidth));

		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				System.out.println(
						"MyView::createComponents()::addMouseListener() -> clicked " + e.getX() + ", " + e.getY());
				setChanged();
				notifyObservers(e);
			}
		});
	}
	
	/**
	 * append something to the shown message
	 * @param message
	 */
	public void appendMessage(String message) {
		messageView.appendMessage(message);
	}

	/**
	 * show a message
	 * @param message
	 */
	public void showMessage(String message) {
		messageView.setMessage(message);
	}

	/**
	 * Refreshing/Repainting the canvas with the new data
	 * 
	 * @param data
	 *            the data used for drawing
	 */
	public void refresh(Map<String, Object> data) {
		canvas.draw((NodeSet[]) data.get("nodeSets"), (Player[]) data.get("players"));
	}

}
