package controller;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import constants.GameState;
import constants.Players;
import model.BoardModel;
import model.Node;
import model.NodeSet;
import model.Piece;
import view.MessageView;
import view.MyView;

public class GameController {
	MyView theView;
	BoardModel theModel;
	MyView myWindow;
	public Players whosTurn = Players.PLAYER1;
	public GameState currentState = GameState.SET;
	public GameState lastState;

	public GameController(MyView theView, BoardModel theModel) {

		this.theView = theView;
		this.theModel = theModel;

		this.theModel.addObserver(this.theView);

		// add mouseListeners to the view
		this.theView.gameView.gridPanel.addMouseListener(new MyMouseListener());
		this.theView.gameView.playerOnePanel.addMouseListener(new MyMouseListener());
		this.theView.gameView.playerTwoPanel.addMouseListener(new MyMouseListener());

		this.theView.messageView
				.setMessage("Player One's turn. " + " --------------- " + "Set one of you piece on the grid.");

	}

	public void paintGamePanel() {
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.theView.gameView.drawGridWithPieces(this.theModel.getNodeSets());
		this.theView.gameView.drawPiecesOnPlayerPanel(this.theModel.getPlayers());
	}

	public Node checkClickedPositionForNode(Point pos) {
		int scale = 70;
		int offSet = 40;
		int rectRadius = 20;
		for (NodeSet set : theModel.getNodeSets()) {
			for (Node node : set.getNodes()) {
				if (node.getX() * scale + offSet > pos.getX() - rectRadius
						&& node.getX() * scale + offSet < pos.getX() + rectRadius
						&& node.getY() * scale + offSet > pos.getY() - rectRadius
						&& node.getY() * scale + offSet < pos.getY() + rectRadius) {
					return node;
				}
			}
		}
		return null;
	}

	public void showMessage(String message) {
		theView.messageView.setMessage(message);
	}

	class MyMouseListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getComponent() == theView.gameView.gridPanel) {
				Node tmp = checkClickedPositionForNode(e.getPoint());
				switch (currentState) {
				case SET:
					if (theModel.setPieceToNodeSet(tmp.getIndex(), whosTurn)) {
						// piece has been set
						System.out.println(whosTurn + " have set: " + tmp.getIndex());
						if (checkMills(tmp, whosTurn)) {
							// check if the current player has made any mills
							showMessage("Mill! " + whosTurn.toString() + " can remove a piece of his opponent");

							lastState = currentState;
							currentState = GameState.TAKE;
						} else { // if we made no mill, the turn goes to the
									// opponent

							// change the turn.
							changeTurn();
							checkIfMovePhase();
						}
					} else {
						System.out.println("cannot set piece here");
					}

					break;
				case TAKE:
					if (theModel.takePiece(tmp.getIndex(), whosTurn)) {
						// piece has been taken
						System.out.println(whosTurn + " have taken: " + tmp.getIndex());
						showMessage(whosTurn + " has removed a piece of his enemy.");

						// manage gameStates
						if (lastState == GameState.SET) {
							lastState = currentState;
							currentState = GameState.SET;
						} else if (lastState == GameState.MOVE) {
							lastState = currentState;
							currentState = GameState.MOVE;
						} else {
							lastState = currentState;
							currentState = GameState.JUMP;
						}

						// change turn
						changeTurn();
					}
					break;

				case MOVE:

					break;

				case JUMP:

					break;
				}

				// TODO: move or set piece if possible
			} else {
				// other panels than gridPanel

			}

			// for debug purpose:
			System.out.println("MouseClicked: " + e.getY() + " | " + e.getX());

		}

		public void checkIfMovePhase() {
			if (theModel.getPlayer(Players.PLAYER1).getPiecesToSet() == 0
					&& theModel.getPlayer(Players.PLAYER2).getPiecesToSet() == 0) {
				lastState = currentState;
				currentState = GameState.MOVE;
				showMessage("Move Phase!  " + whosTurn + " 's turn. Move a piece.");

			}

		}

		public void changeTurn() {
			if (whosTurn == Players.PLAYER1) {
				whosTurn = Players.PLAYER2;
			} else {
				whosTurn = Players.PLAYER1;
			}

		}

		public boolean checkMills(Node dest, Players owner) {
			for (NodeSet set : theModel.getNodeSets()) {
				if (set.hasMillFromPlayer() == owner && set.containsNode(dest)) {
					return true;
				}
			}
			return false;
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

}
