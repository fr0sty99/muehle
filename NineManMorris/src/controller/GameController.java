package controller;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import constants.Players;
import model.BoardModel;
import model.Node;
import model.NodeSet;
import model.Piece;
import view.MyView;

public class GameController {
	MyView theView;
	BoardModel theModel;
	MyView myWindow;
	public Players whosTurn = Players.PLAYER1;

	public GameController(MyView theView, BoardModel theModel) {

		this.theView = theView;
		this.theModel = theModel;

		this.theModel.addObserver(this.theView);

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

	class MyMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getComponent() == theView.gameView.gridPanel) {

				Node tmp = checkClickedPositionForNode(e.getPoint());
				if (tmp != null && tmp.isEmpty()) {
					if (whosTurn == Players.PLAYER1) {
						if (theModel.getPlayer(Players.PLAYER1).getPiecesToSet() > 0) {

							if (theModel.setPieceToNodeSet(tmp.getIndex(), new Piece(Players.PLAYER1))) {
								System.out.println("Player1 have set: " + tmp.getIndex());
								theView.messageView.setMessage(
										"Player Two's turn. ---------------------- Set one of your pieces on the grid");
								whosTurn = Players.PLAYER2;

							} else {
								System.out.println("you cant set a piece here");
							}

						} else {
							System.out.println("you have no more pieces to set");
						}
					} else {
						if (theModel.getPlayer(Players.PLAYER2).getPiecesToSet() > 0) {

							if (theModel.setPieceToNodeSet(tmp.getIndex(), new Piece(Players.PLAYER2))) {
								System.out.println("Player2 have set: " + tmp.getIndex());
								theView.messageView.setMessage(
										"Player One's turn. ---------------------- Set one of your pieces on the grid");
								whosTurn = Players.PLAYER1;
							} else {
								System.out.println("you cant set a piece here");
							}

						} else {
							System.out.println("you have no more pieces to set");
						}
					}
				} else {
					System.out.println("setting here is not possible");
				}

				// TODO: move or set piece if possible
			} else {
				if (e.getComponent() == theView.gameView.playerPanel.getLeftComponent()) {

				}
			}
			System.out.println("MouseClicked: " + e.getY() + " | " + e.getX());

			if (theModel.getPlayer(Players.PLAYER1).getPiecesToSet() == 0
					&& theModel.getPlayer(Players.PLAYER2).getPiecesToSet() == 0) {
				System.out.println("move phase");
			}
			
			Players possibleMillPlayer = checkMills();
			
			if(possibleMillPlayer == Players.PLAYER1) {
				System.out.println("Mill! player1 can remove a piece of player2 now");
			} else if(possibleMillPlayer == Players.PLAYER2) {
				System.out.println("Mill! player2 can remove a piece of player1 now");

			}

		}
		
		public Players checkMills() {
			for(NodeSet set : theModel.getNodeSets()) {
				return set.hasMillFromPlayer();
			}
			return Players.NOPLAYER;
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
