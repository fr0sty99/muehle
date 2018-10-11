package view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JPanel;

import constants.AppColors;
import constants.Owner;
import model.Node;
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

	private final int distanceBetweenNodes = 70; // dist
	private int gridPanelmarginOffset = 40; // distance from border for the grid
	private final int gridPanelPieceSize = 30; // diameter of piece

	// public so we can access our data and UI elements
	public MessageView messageView;
	public GameView gameView;

	public MyView(Observer observer) {
		createWindow();
		addObserver(observer);
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

	// /** TODO WIP
	// * adds a mouseListener to our contentPane
	// * @param listener the MouseListener to add
	// */
	// public void addMyMouseListener(MouseListener listener) {
	// frame.getContentPane().addMouseListener(listener);
	// }

	public MyCanvas getCanvas() {
		return canvas;
	}
	/**
	 * creates the components used for this frame
	 */
	public void createComponents() {
		messageView = new MessageView(screenWidth, 80);

		// create canvas
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

	// /** TODO WIP
	// * gets called when the model calls notifyObserver. for updating the GUI
	// */
	// @Override
	// public void update(Observable o, Object arg) {
	// gameView.drawPiecesOnPlayerPanel(((Board) arg).getPlayers());
	// gameView.drawGridWithPieces(((Board) arg).getNodeSets());
	// }

	public void refresh(Map<String, Object> data) {
		canvas.draw((NodeSet[]) data.get("nodeSets"), (Player[]) data.get("players"));
	}

	/**
	 * Inner class DrawCanvas (extends JPanel) used for drawing
	 */
	class MyCanvas extends JPanel {
		private static final long serialVersionUID = -1096245432319028462L;
		NodeSet[] grid;
		Player[] players;

		public void draw(NodeSet[] grid, Player[] players) {
			this.grid = grid;
			this.players = players;
			repaint();
		}

		/**
		 * draws a piece on a Node
		 * 
		 * @param g
		 *            the Graphics obj used for drawing
		 * @param n
		 *            the node to draw on
		 */
		public void drawPieceOnNode(Graphics2D g, Node n) {
			// set color
			if (n.getOwner() == Owner.WHITE) {
				g.setColor(AppColors.whitePlayerColor);
			} else {
				g.setColor(AppColors.blackPlayerColor);
			}

			// draw piece
			if (n.hasOwner() && n.getOwner() != Owner.EMPTY) {
				g.fillOval(n.getX() * distanceBetweenNodes + gridPanelmarginOffset - 30 / 2,
						n.getY() * distanceBetweenNodes + gridPanelmarginOffset - 30 / 2, gridPanelPieceSize,
						gridPanelPieceSize);
			}

			// draw selection circle
			if (n.isSelected()) {
				g.setColor(Color.PINK);
				g.drawOval(n.getX() * distanceBetweenNodes + gridPanelmarginOffset - 30 / 2,
						n.getY() * distanceBetweenNodes + gridPanelmarginOffset - 30 / 2, gridPanelPieceSize,
						gridPanelPieceSize);
			}
		}

		/**
		 * draws the grid and its pieces on the gridPanel
		 * 
		 * @param sets
		 *            the nodeSets used for drawing
		 */
		public void drawGridWithPieces(Graphics2D g) {
			System.out.println(g);
			g.clearRect(0, 0, frame.getWidth(), frame.getHeight()); // clear
																	// panel

			// draw grid
			for (NodeSet nodeSet : grid) {
				// ATTENTION: if "sets"-Array is not filled correctly by the
				// createNodeSets method in GameController, this create a
				// nullPointer
				Node firstNode = nodeSet.getFirstNode();
				Node secondNode = nodeSet.getSecondNode();
				Node thirdNode = nodeSet.getThirdNode();

				g.setStroke(new BasicStroke(10));
				g.setColor(AppColors.gridColor);

				// draw a thick line from first to last Node of every NodeSet
				g.drawLine(firstNode.getX() * distanceBetweenNodes + gridPanelmarginOffset,
						firstNode.getY() * distanceBetweenNodes + gridPanelmarginOffset,
						thirdNode.getX() * distanceBetweenNodes + gridPanelmarginOffset,
						thirdNode.getY() * distanceBetweenNodes + gridPanelmarginOffset);

				drawPieceOnNode(g, firstNode);
				drawPieceOnNode(g, secondNode);
				drawPieceOnNode(g, thirdNode);
			}
			
			System.out.println("repaint finished");;
		}

		@Override
		public void paintComponent(Graphics g) { // invoke via repaint()
			super.paintComponent(g); // fill background
			setBackground(Color.WHITE); // set its background color

			// // Draw the grid-lines TODO
			if (grid != null && players != null) {
				drawGridWithPieces((Graphics2D) g);
			}
		}
	}

}
