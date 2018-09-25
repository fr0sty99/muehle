package controller;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import constants.Players;
import model.BoardModel;
import model.Node;
import model.Piece;
import view.MyWindow;

public class GameController {
	MyWindow theView;
	BoardModel theModel;
	MyWindow myWindow;
	public boolean dragging = false;
	public Players whosTurn = Players.PLAYER1;

	public GameController(MyWindow theView, BoardModel theModel) {

		this.theView = theView;
		this.theModel = theModel;

		// add mouseListeners to the view
		this.theView.gameView.gridPanel.addMouseListener(new MyMouseListener());
		this.theView.gameView.playerOnePanel.addMouseListener(new MyMouseListener());
		this.theView.gameView.playerTwoPanel.addMouseListener(new MyMouseListener());
		
		System.out.println("paintGrid");

		// TODO: tell the View to create the Grid with the nodes and the
		// playerView with the piecesToSet with the data from the model

		this.theView.messageView
				.setMessage("Player One's turn. " + " --------------- " + "Set one of you piece on the grid.");

	}

	public void paintGamePanel() {
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.theView.gameView.drawGridWithPieces(this.theModel.getNodeSets());
		this.theView.gameView.drawPiecesOnPlayerPanel(this.theModel.getPlayers());
	}

	public void draw(Graphics g, Piece[] board) {

		for (Piece piece : board) {
			switch (piece.belongsTo) {
			case PLAYER1:
				// g.drawRect(piece.index, y, width, height);
				break;
			case PLAYER2:

				break;

			case NOPLAYER:

				break;
			default:

				break;
			}
		}
	}

	public Node checkClickedPositionForNode(Point pos) {
		int scale = 70;
		int offSet = 40;
		int rectRadius = 20;
		for (Node node : theModel.getNodeList()) {
			if (node.getX() * scale + offSet > pos.getX() - rectRadius
					&& node.getX() * scale + offSet < pos.getX() + rectRadius
					&& node.getY() * scale + offSet > pos.getY() - rectRadius
					&& node.getY() * scale + offSet < pos.getY() + rectRadius) {
				return node;
			}
		}
		return null;
	}

	public void dragPieceIfPossible(Players player) {
			if (whosTurn == player && !dragging) {
				dragging = true;
				// dragging allowed

				// draw piece until set
				// while "dragging" drag.. -> add this to draw Loop
			}
		
	}

	class MyMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getComponent() == theView.gameView.gridPanel) {
				
				if (dragging) {
					dragging = false;
					Node tmp = checkClickedPositionForNode(e.getPoint());
					if (tmp != null) {
						System.out.println("you have set: " + tmp.getIndex());
					}
				}
				
				// TODO: move or set piece if possible
			} else {
				if (e.getComponent() == theView.gameView.playerPanel.getLeftComponent()) {
					// player ONE
					dragPieceIfPossible(Players.PLAYER1);
				} else {
					// plaer TWO
					dragPieceIfPossible(Players.PLAYER2);
				}
			}
			System.out.println("MouseClicked: " + e.getY() + " | " + e.getX());

		}

		@Override
		public void mousePressed(MouseEvent e) {
			System.out.println("MousePressed: " + e.getY() + " | " + e.getX());
			// TODO: move or set piece if possible
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			System.out.println("MouseReleased: " + e.getY() + " | " + e.getX());
			// TODO: move or set piece if possible
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			System.out.println("MouseEntered on " + e.getComponent().getName() + ": " + e.getY() + " | " + e.getX());
			// TODO: move or set piece if possible
		}

		@Override
		public void mouseExited(MouseEvent e) {
			System.out.println("MouseExited on " + e.getComponent().getName() + e.getY() + " | " + e.getX());
			// TODO: move or set piece if possible
		}
	}

	public void runGame() {
		// Phase 1
		// Set Pieces

		// 1. set Message: its whites turn, you have to set a piece to the board
		// 2. wait for playerInput ( drag & drop )

		// Phase 2
		// Move Pieces

		// Phase 3 (End-Phase)
		// Jump with Pieces
	}

}
