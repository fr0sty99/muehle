package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.PopupMenu;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.RepaintManager;

import constants.AppColors;
import constants.Owner;
import model.Node;
import model.NodeSet;
import model.Player;

/**
 *  This class is responsible for drawing the Board and its Pieces
 */
@SuppressWarnings("serial")
public class GameView extends JSplitPane {
	public int scale = 70;
	public int marginOffset = 40;
	public JPanel gridPanel;
	public JSplitPane playerPanel;
	public int dividerLocation = 0;
	private final int pieceSize = 30;

	public JPanel playerOnePanel;
	public JPanel playerTwoPanel;

	public GameView(int gridViewWidth, int gridViewHeight) {
		// board
		Dimension boardPanelDimension = new Dimension(gridViewWidth, gridViewHeight);
		setBackground(AppColors.panelDefault);
		setPreferredSize(boardPanelDimension);
		setMaximumSize(boardPanelDimension);

		setDividerSize(0);
		dividerLocation = gridViewHeight - 50;
		setDividerLocation(gridViewHeight);

		setOrientation(JSplitPane.VERTICAL_SPLIT);

		// init Labels for playerPanel
		playerOnePanel = new JPanel();
		playerTwoPanel = new JPanel();

		playerOnePanel.setLayout(new GridBagLayout());
		playerTwoPanel.setLayout(new GridBagLayout());

		gridPanel = new JPanel();
		gridPanel.setName("gridPanel"); // debug

		playerPanel = new JSplitPane();

		Dimension size = new Dimension(gridViewWidth, 20);
		playerPanel.setMinimumSize(size);
		playerPanel.setMaximumSize(size);

		setTopComponent(gridPanel);
		setBottomComponent(playerPanel);

		// init Panels
		playerPanel.setDividerSize(0);
		playerPanel.setDividerLocation(gridViewHeight / 2);
		playerPanel.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		playerPanel.setLeftComponent(playerOnePanel);
		playerPanel.setRightComponent(playerTwoPanel);
	}

	public void drawPiecesOnPlayerPanel(Player[] players) {
		// draw pieces
		Graphics2D leftGraphics = (Graphics2D) playerPanel.getLeftComponent().getGraphics();
		Graphics2D rightGraphics = (Graphics2D) playerPanel.getRightComponent().getGraphics();

		int offSetX = 40;
		int offSetY = 10;
		int dist = 20;
		int x = 0, y = 0;
		int width = 10, height = 10;

		leftGraphics.clearRect(0, 0, 300, 300);
		for (int i = 0; i < players[0].getPiecesToSet(); i++) {
			leftGraphics.setColor(Color.WHITE);

			leftGraphics.fillOval(x + offSetX, y + offSetY, width, height);
			x += dist;
		}

		x = 0; // reset coords

		rightGraphics.clearRect(0, 0, 300, 300);

		for (int i = 0; i < players[1].getPiecesToSet(); i++) {
			rightGraphics.setColor(Color.BLACK);

			rightGraphics.fillOval(x + offSetX, y + offSetY, width, height);
			x += dist;
		}
	}

	public void addMyMouseListener(MouseListener mouseListener) {
		addMouseListener(mouseListener);
	}

	public void drawGridWithPieces(NodeSet[] sets) {
		Graphics2D g = (Graphics2D) gridPanel.getGraphics();
		g.clearRect(0, 0, gridPanel.getWidth(), gridPanel.getHeight());
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
			g.drawLine(firstNode.getX() * scale + marginOffset, firstNode.getY() * scale + marginOffset,
					thirdNode.getX() * scale + marginOffset, thirdNode.getY() * scale + marginOffset);

			drawPieceOnNode(g, firstNode);
			drawPieceOnNode(g, secondNode);
			drawPieceOnNode(g, thirdNode);
		}
	}

	public void drawPieceOnNode(Graphics2D g, Node n) {
		if (n.getOwner() == Owner.WHITE) {
			g.setColor(AppColors.playerOneColor);
		} else {
			g.setColor(AppColors.playerTwoColor);
		}

		if (n.hasOwner() && n.getOwner() != Owner.EMPTY) {
			g.fillOval(n.getX() * scale + marginOffset - 30 / 2, n.getY() * scale + marginOffset - 30 / 2, pieceSize, pieceSize);
		}

		if (n.isSelected()) {
			g.setColor(Color.PINK);
			g.drawOval(n.getX() * scale + marginOffset - 30 / 2, n.getY() * scale + marginOffset - 30 / 2, pieceSize, pieceSize);
		}
	}

}
