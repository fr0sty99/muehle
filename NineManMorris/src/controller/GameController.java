package controller;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import constants.GameState;
import constants.Owner;
import model.BoardModel;
import model.Node;
import model.NodeSet;
import model.Player;
import view.MyView;

public class GameController {
	MyView theView;
	BoardModel theModel;
	MyView myWindow;
	public Owner whosTurn = Owner.PLAYER1;
	public GameState currentState = GameState.SET;
	public GameState lastState;

	public Node selectedNode;

	public GameController(MyView theView, BoardModel theModel) {
		this.theView = theView;
		this.theModel = theModel;

		this.theModel.addObserver(this.theView);

		// add mouseListeners to the view
		this.theView.gameView.gridPanel.addMouseListener(new MyMouseListener());
		this.theView.gameView.playerOnePanel.addMouseListener(new MyMouseListener());
		this.theView.gameView.playerTwoPanel.addMouseListener(new MyMouseListener());
		
		this.theView.messageView.setMessage(whosTurn + "'s turn. Set one of your pieces on the grid.");

	}

	public void paintGamePanel() {
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.theView.gameView.drawGridWithPieces(this.theModel.getNodeSets());
		this.theView.gameView.drawPiecesOnPlayerPanel(this.theModel.getPlayers());
	}

	/**
	 * finds the node of the clicked position
	 * @param pos Point coordinates of the click
	 * @return the clicked node
	 */
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

	/**
	 * shows a message in the messageView
	 * @param message the message to be displayed
	 */
	public void showMessage(String message) {
		theView.messageView.setMessage(message);
	}

	/**
	 * process our clicks and determine what to do in which gameState
	 * @param e the MouseEvent / Click
	 */
	public void processClick(MouseEvent e) {
		if (e.getComponent() == theView.gameView.gridPanel) {

			// get Node at clickPosition
			Node tmp = checkClickedPositionForNode(e.getPoint());
			if (tmp != null) {

				switch (currentState) {
				case SET:
					System.out.println("SET");

					if (theModel.setNode(tmp.getIndex(), whosTurn)) {
						// piece has been placed successfully

						if (theModel.checkMills(tmp, whosTurn)) {
							// setting this piece has made a mill
							showMessage("Mill! " + whosTurn.toString() + " can remove a piece of his opponent.");

							lastState = currentState;
							currentState = GameState.TAKE;
						} else {
							// if the player created no mill with this move,
							// its the other players turn
							changeTurn();
							showMessage(whosTurn + " turn. Set one of you piece on the grid.");
							checkIfMovePhase();
						}
					} else {
						// piece can't be set here
						showMessage(whosTurn + "! You cant set your piece here. Please pick another location.");
					}
					break;

				case TAKE:
					System.out.println("TAKE");

					if (theModel.takePiece(tmp.getIndex(), whosTurn)) {
						// piece has been taken successfully

						currentState = lastState;
						changeTurn();
						
						showMessage(whosTurn + "'s turn. Set one of your pieces on the grid.");
						checkIfMovePhase();
						checkIfJumpPhase();
					} else {
						showMessage(whosTurn + "! You cant take this piece! Please choose another.");
					}
					break;
				case MOVE:
					System.out.println("MOVE");

					if (selectedNode == null) {
						if (tmp.getOwner() == whosTurn) {
							tmp.setSelected(true);
							theModel.notifyDataSetChanged();
							selectedNode = tmp;
							showMessage(whosTurn + " has selected a piece for moving.");
						} else {
							showMessage(whosTurn
									+ "! You cant move the piece of your opponent. Please select another piece.");
						}
					} else {
						if (tmp.getIndex() == selectedNode.getIndex()) {
							if (tmp.isSelected()) {
								tmp.setSelected(false);
								selectedNode = null;
								theModel.notifyDataSetChanged();
								showMessage(whosTurn
										+ " has de-selected a piece for moving. You can choose another piece now.");
							}
						} else {
							if (theModel.movePiece(selectedNode, tmp)) {
								// piece moved successfully
								// de-select node
								selectedNode.setSelected(false);
								selectedNode = null;
								theModel.notifyDataSetChanged();

								showMessage(whosTurn + " has moved a Piece.");

								if (theModel.checkMills(tmp, whosTurn)) {
									// setting this piece has made a mill
									showMessage(
											"Mill! " + whosTurn.toString() + " can remove a piece of his opponent.");

									lastState = currentState;
									currentState = GameState.TAKE;
								} else {
									// if the player created no mill with
									// this move,
									// its the other players turn
									// check and set gameState
									currentState = GameState.MOVE;
									changeTurn();
									checkIfJumpPhase();
								}
							} else {
								showMessage(whosTurn + "! You cant move here. Please try another location.");
							}
						}
					}
					break;
				case JUMP:
					if (selectedNode == null) {
						if (tmp.getOwner() == whosTurn) {
							// if we haven't selected a node and the clicked
							// node belongs to us it gets selected
							tmp.setSelected(true);
							theModel.notifyDataSetChanged();
							selectedNode = tmp;
							showMessage(whosTurn + " has selected a node for jumping.");
						}
					} else {
						if (tmp.getIndex() == selectedNode.getIndex()) {
							// if we click on the already selected node, it
							// gets de-selected
							if (tmp.isSelected()) {
								tmp.setSelected(false);
								selectedNode = null;
								theModel.notifyDataSetChanged();
								showMessage(whosTurn + " has de-selected a node for jumping.");
							}
						} else if (theModel.jumpPiece(selectedNode, tmp)) {
							// piece jumped successfully
							selectedNode.setSelected(false);
							selectedNode = null;
							theModel.notifyDataSetChanged();

							showMessage(whosTurn + " has jumped with a Piece.");

							if (theModel.checkMills(tmp, whosTurn)) {
								lastState = currentState;
								currentState = GameState.TAKE;
								showMessage("Mill! " + whosTurn.toString() + " can remove a piece of his opponent.");
							} else {
								changeTurn();
								checkIfMovePhase();
								checkIfJumpPhase();
							}
						} else {
							showMessage(whosTurn + "! You cant jump here. Please select another destination.");
						}
					}
					break;
				case GAMEOVER:
					// TODO: show rematch button
					break;
				}
			} else {
				// clicked other panel than gridPanel

			}
		}

		// check if game is over
		if (checkForLose() != Owner.NOPLAYER) {
			currentState = GameState.GAMEOVER;
			showMessage("GAMEOVER! " + theModel.getOtherPlayer(whosTurn) + " has won!");
		}
	}

	/**
	 * MouseListener for clicks on the board
	 */
	class MyMouseListener implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			processClick(e);
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {

		}

		@Override
		public void mouseEntered(MouseEvent e) {

		}

		@Override
		public void mouseExited(MouseEvent e) {

		}
	}

	/**
	 * checks if any of the players has lost case A: no more pieces to form a
	 * mill case B: blocked to make a move
	 * 
	 * @return the looser
	 */
	public Owner checkForLose() {
		if (currentState != GameState.SET && currentState != GameState.TAKE) {
			for (Player player : theModel.getPlayers()) {
				if (player.getPiecesOnBoard() < 3) {
					return player.isOwner();
				} else if (theModel.isPlayerBlocked(theModel.getPlayer(Owner.PLAYER1))) {
					return Owner.PLAYER1;
				} else if (theModel.isPlayerBlocked(theModel.getPlayer(Owner.PLAYER2))) {
					return Owner.PLAYER2;
				}
			}
		}
		return Owner.NOPLAYER;
	}

	/**
	 * checks if the player which is on turn next has to jump instead of moving
	 * (when he has only 3 pieces left) if yes, the GameState gets changed to
	 * JUMP
	 */
	public void checkIfJumpPhase() {
		if (currentState != GameState.SET || lastState != GameState.SET) {
			if (theModel.getPlayer(whosTurn).getPiecesOnBoard() <= 3) {
				lastState = currentState;
				currentState = GameState.JUMP;
				showMessage(whosTurn + "'s turn. Jump with a piece.");
			}
		}
	}

	/**
	 * checks if all pieces have been set if yes, the GameState gets changed to
	 * MOVE
	 */
	public void checkIfMovePhase() {
		if (theModel.getPlayer(Owner.PLAYER1).getPiecesToSet() == 0
				&& theModel.getPlayer(Owner.PLAYER2).getPiecesToSet() == 0) {
			lastState = currentState;
			currentState = GameState.MOVE;
			showMessage(whosTurn + "'s turn. Move a piece.");
		}
	}

	/**
	 * changes whos turn it is depending on the current player
	 */
	public void changeTurn() {
		if (whosTurn == Owner.PLAYER1) {
			whosTurn = Owner.PLAYER2;
		} else {
			whosTurn = Owner.PLAYER1;
		}
	}
}
