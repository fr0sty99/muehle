package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import constants.AppColors;
import constants.Owner;
import model.Node;
import model.NodeSet;
import model.Player;

/**
 * The Canvas (extends JPanel) used for drawing
 */
@SuppressWarnings("serial")
class MyCanvas extends JPanel {
	private NodeSet[] gridData;
	private Player[] playersData;
	private final int distanceBetweenNodes = 70; // dist
	private final int gridPanelmarginOffset = 40; // distance from border for
													// the grid
	private final int gridPanelPieceSize = 30; // diameter of piece

	/**
	 * Re-draws the grid and the player.. gets called from MyView::refresh()
	 * 
	 * @param grid
	 *            the data for the grid
	 * @param players
	 *            the data for the players
	 */
	void draw(NodeSet[] grid, Player[] players) {
		this.gridData = grid;
		this.playersData = players;
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
	private void drawPieceOnNode(Graphics2D g, Node n) {
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
	private void drawGridWithPieces(Graphics2D g) {

		// draw grid
		for (NodeSet nodeSet : gridData) {
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

		System.out.println("repaint finished");
	}

	/**
	 * draws the boards pieces on the grids
	 * 
	 * @param playersData
	 *            the players, used for counting the pieces to draw
	 */
	private void drawPiecesUnderGrid(Graphics2D g) {

		int playerPanelPieceSize = 20; // diameter of piece
		int playerPanelOffSetX = 40; // distance from border
		int playerPanelOffSetY = 10;// distance from border
		int playerPanelPieceDist = 20; // distance between pieces

		g.setColor(AppColors.panelDefault);
		g.fillRect(0, 500, 500, 30); // clear panel

		int x = 0, y = 480; // coords
		g.setColor(Color.PINK);

		for (int i = 0; i < playersData[0].getPiecesToSet(); i++) {
			// draw pieces
			g.setColor(AppColors.whitePlayerColor);
			g.fillOval(x + playerPanelOffSetX, y + playerPanelOffSetY, playerPanelPieceSize, playerPanelPieceSize);
			x += playerPanelPieceDist;
		}

		x = 20 * playerPanelPieceDist; // set position with offSet
		for (int i = playersData[1].getPiecesToSet(); i > 0; i--) {
			// draw pieces
			g.setColor(AppColors.blackPlayerColor);
			g.setStroke(new BasicStroke(1));

			g.fillOval(x + playerPanelOffSetX, y + playerPanelOffSetY, playerPanelPieceSize, playerPanelPieceSize);
			x -= playerPanelPieceDist;
		}
	}

	@Override
	public void paintComponent(Graphics g) { // invoke via repaint()
		super.paintComponent(g);

		// clear canvas
		setBackground(AppColors.panelDefault);

		// draw our game
		if (gridData != null && playersData != null) {
			drawGridWithPieces((Graphics2D) g);
			drawPiecesUnderGrid((Graphics2D) g);
		}
	}
}
