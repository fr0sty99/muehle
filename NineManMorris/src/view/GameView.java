package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import constants.AppColors;
import constants.Owner;
import model.Node;
import model.NodeSet;
import model.Player;

/**
 * This class is responsible for drawing the Board and its Pieces
 */
public class GameView extends JSplitPane {
	private static final long serialVersionUID = 834088465149698366L;

	// finals
	private final int distanceBetweenNodes = 70; // dis
	private int gridPanelmarginOffset = 40; // distance from border for the grid
	private final int gridPanelPieceSize = 30; // diameter of piece
	private int playerPanelPieceSize = 10; // diameter of piece
	private int playerPanelOffSetX = 40; // distance from border
	private int playerPanelOffSetY = 10;// distance from border
	private int playerPanelPieceDist = 20; // distance between pieces

	// Panels
	private JSplitPane playerPanel;
	public JPanel gridPanel;
	public JPanel playerOnePanel;
	public JPanel playerTwoPanel;

	/**
	 * initializes a new GameView, which holds a gridView (board), playerView
	 * (pieces to set) and messageView (informing the player)
	 * 
	 * @param gridViewWidth
	 *            width of the view
	 * @param gridViewHeight
	 *            height of the view
	 */
	public GameView(int gridViewWidth, int gridViewHeight) {
		// setting up boardPanel
		Dimension boardPanelDimension = new Dimension(gridViewWidth, gridViewHeight);
		setBackground(AppColors.panelDefault);
		setPreferredSize(boardPanelDimension);
		setMaximumSize(boardPanelDimension);
		setDividerSize(0);
		setDividerLocation(gridViewHeight);
		setOrientation(JSplitPane.VERTICAL_SPLIT);

		// setting up playerPanel-components
		playerOnePanel = new JPanel();
		playerTwoPanel = new JPanel();
		playerOnePanel.setLayout(new GridBagLayout());
		playerTwoPanel.setLayout(new GridBagLayout());

		// setting up gridPanel
		gridPanel = new JPanel();
		gridPanel.setName("gridPanel"); // debug

		// setting up playerPanel
		playerPanel = new JSplitPane();
		Dimension size = new Dimension(gridViewWidth, 20);
		playerPanel.setMinimumSize(size);
		playerPanel.setMaximumSize(size);

		// adding gridPanel and PlayerPanel to this (JSplitPane)
		setTopComponent(gridPanel);
		setBottomComponent(playerPanel);

		// configuring playerPanel
		playerPanel.setDividerSize(0);
		playerPanel.setDividerLocation(gridViewHeight / 2);
		playerPanel.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		playerPanel.setLeftComponent(playerOnePanel);
		playerPanel.setRightComponent(playerTwoPanel);
	}

	/**
	 * draws the boards pieces on the grids
	 * 
	 * @param players
	 *            the players, used for counting the pieces to draw
	 */
	public void drawPiecesOnPlayerPanel(Player[] players) {
		// get Graphics of the panels
		Graphics2D leftGraphics = (Graphics2D) playerPanel.getLeftComponent().getGraphics();
		Graphics2D rightGraphics = (Graphics2D) playerPanel.getRightComponent().getGraphics();
		
		int x = 0, y = 0; // coords
		leftGraphics.clearRect(0, 0, 300, 300); // clear panel
		for (int i = 0; i < players[0].getPiecesToSet(); i++) {
			// draw pieces
			leftGraphics.setColor(AppColors.whitePlayerColor);
			leftGraphics.fillOval(x + playerPanelOffSetX, y + playerPanelOffSetY, playerPanelPieceSize, playerPanelPieceSize);
			x += playerPanelPieceDist;
		}

		x = 0; // reset coords
		rightGraphics.clearRect(0, 0, 300, 300); // clear panel
		for (int i = 0; i < players[1].getPiecesToSet(); i++) {
			// draw pieces
			rightGraphics.setColor(AppColors.blackPlayerColor);
			rightGraphics.fillOval(x + playerPanelOffSetX, y + playerPanelOffSetY, playerPanelPieceSize, playerPanelPieceSize);
			x += playerPanelPieceDist;
		}
	}

	public void addMyMouseListener(MouseListener mouseListener) {
		addMouseListener(mouseListener);
	}

	/**
	 * draws the grid and its pieces on the gridPanel
	 * @param sets the nodeSets used for drawing
	 */
	public void drawGridWithPieces(NodeSet[] sets) {
		Graphics2D g = (Graphics2D) gridPanel.getGraphics();
		System.out.println(g);
		g.clearRect(0, 0, gridPanel.getWidth(), gridPanel.getHeight()); // clear panel
		
		// draw grid
		for (NodeSet nodeSet : sets) {
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
	}

	/**
	 * draws a piece on a Node
	 * @param g the Graphics obj used for drawing
	 * @param n the node to draw on
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
					n.getY() * distanceBetweenNodes + gridPanelmarginOffset - 30 / 2, gridPanelPieceSize, gridPanelPieceSize);
		}

		// draw selection circle 
		if (n.isSelected()) {
			g.setColor(Color.PINK);
			g.drawOval(n.getX() * distanceBetweenNodes + gridPanelmarginOffset - 30 / 2,
					n.getY() * distanceBetweenNodes + gridPanelmarginOffset - 30 / 2, gridPanelPieceSize, gridPanelPieceSize);
		}
	}

}
