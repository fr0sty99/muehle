package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import constants.AppColors;
import model.Node;
import model.NodeSet;
import model.Player;

// This class is responsible for representing the board aka playground.
@SuppressWarnings("serial")
public class GameView extends JSplitPane implements Observer{
	public int scale = 70;
	public int offSetY = 40;
	public JPanel gridPanel;
	public JSplitPane playerPanel;
	public int dividerLocation = 0;

	public JPanel playerOnePanel;
	public JPanel playerTwoPanel;
	public JLabel playerOneLabel;
	public JLabel playerTwoLabel;

	public GameView(int gridViewWidth, int gridViewHeight) {
		// board
		Dimension boardPanelDimension = new Dimension(gridViewWidth, gridViewHeight);
		setBackground(AppColors.panelDefaultColor);
		setPreferredSize(boardPanelDimension);
		setMaximumSize(boardPanelDimension);

		setDividerSize(0);
		dividerLocation = gridViewHeight - 50;
		setDividerLocation(gridViewHeight);

		setOrientation(JSplitPane.VERTICAL_SPLIT);

		// init Labels for playerPanel
		playerOneLabel = new JLabel("Player ONE");
		playerTwoLabel = new JLabel("Player TWO");

		playerOnePanel = new JPanel();
		playerTwoPanel = new JPanel();
		playerOnePanel.setName("panelPLAYERONE"); // debug 
		playerTwoPanel.setName("panelPLAYERTWO"); // debug

		playerOnePanel.setLayout(new GridBagLayout());
		playerTwoPanel.setLayout(new GridBagLayout());

		playerOnePanel.add(playerOneLabel);
		playerTwoPanel.add(playerTwoLabel);

		gridPanel = new JPanel();
		gridPanel.setName("gridPanel"); // debug

		playerPanel = new JSplitPane();

		Dimension minimumSize = new Dimension(50, 50);
		playerPanel.setMinimumSize(minimumSize);

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

		int offSetX = 50;
		int offSetY = 10;
		int dist = 20;
		int x = 0, y = 0;
		int width = 10, height = 10;

		for (int i = 0; i < players[0].getPiecesToSet(); i++) {

			leftGraphics.setColor(Color.WHITE);

			leftGraphics.fillOval(x + offSetX, y + offSetY, width, height);
			x += dist;
		}

		x = 0; // reset coords

		for (int i = 0; i < players[1].getPiecesToSet(); i++) {
			rightGraphics.setColor(Color.BLACK);

			rightGraphics.fillOval(x + offSetX, y + offSetY, width, height);
			x += dist;
		}
	}

	public void setPlayerOneLabel(String text) {
		// TODO: implement
	}

	public void setPlayerTwoLabel(String text) {
		// TODO: implement
	}

	public void addMyMouseListener(MouseListener mouseListener) {
		addMouseListener(mouseListener);
	}

	public void drawGridWithPieces(NodeSet[] sets) {
		Graphics2D g = (Graphics2D) gridPanel.getGraphics();
		// draw grid
		for (NodeSet nodeSet : sets) {

			// attention: if "sets"-Array is not filled correctly by the
			// createNodeSets method in GameController, we create a nullPointer
			Node startNode = nodeSet.getFirstNode();
			Node destinationNode = nodeSet.getThirdNode();

			g.setStroke(new BasicStroke(10));

			// draw a thick line from every first to every last Node of every
			// NodeSet
			g.drawLine(startNode.getX() * scale + offSetY, startNode.getY() * scale + offSetY,
					destinationNode.getX() * scale + offSetY, destinationNode.getY() * scale + offSetY);
		}

		// draw pieces on grid

	}

	@Override
	public void update(Observable o, Object arg) {
		//who called us and what did they send?
		System.out.println ("View      : Observable is " + o.getClass() + ", object passed is " + arg.getClass());

	}

}