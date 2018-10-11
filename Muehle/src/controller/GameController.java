package controller;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.security.Key;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;

import constants.GameState;
import constants.Owner;
import model.Board;
import model.Node;
import model.NodeSet;
import model.Player;
import view.MyView;

/**
 * This class is responsible for controlling the behavior of the model and the
 * view
 * 
 * @author Joris
 */
public class GameController implements Observer {
	private MyView theView;
	private Board theModel;
	private Owner whosTurn = Owner.WHITE;
	private GameState currentState = GameState.SET;
	private GameState lastState;
	private int clickRadius = 40;

	public GameController() {
		theView = new MyView(this);
		
		// create game here
		theModel = new Board(this);

		// set default message
		this.theView.messageView.setMessage(whosTurn + "'s turn. Set one of your pieces on the grid.");
	}

	/**
	 * finds the node of the clicked position
	 * 
	 * @param pos
	 *            Point coordinates of the click
	 * @return the clicked node
	 */
	public Node checkClickedPositionForNode(Point pos) {
		int scale = 70;
		int offSet = 40;
		for (NodeSet set : theModel.getNodeSets()) {
			for (Node node : set.getNodes()) {
				if (node.getX() * scale + offSet > pos.getX() - clickRadius
						&& node.getX() * scale + offSet < pos.getX() + clickRadius
						&& node.getY() * scale + offSet > pos.getY() - clickRadius
						&& node.getY() * scale + offSet < pos.getY() + clickRadius) {
					return node;
				}
			}
		}
		return null;
	}

	/**
	 * shows a message in the messageView
	 * 
	 * @param message
	 *            the message to be displayed
	 */
	public void showMessage(String message) {
		theView.messageView.setMessage(message);
	}

	/**
	 * process our clicks and determine what to do in which gameState
	 * 
	 * @param e
	 *            the MouseEvent / Click
	 */
	public void processClick(MouseEvent e) {
		MouseEvent x = e;
		System.out.println(x.getComponent());

		// get Node at clickPosition
		Node tmp = checkClickedPositionForNode(e.getPoint());
		if (tmp != null) {

			switch (currentState) {
			case SET:
				System.out.println("SET");

				if (theModel.setPiece(tmp.getIndex(), whosTurn)) {
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
						showMessage(whosTurn + "'s turn. Set one of your pieces on the grid.");
						checkIfMovePhase();
					}

				} else {
					// piece can't be set here
					showMessage(whosTurn + "! You cant set your piece here. Please pick another location.");
				}
				break;

			case TAKE:
				System.out.println("TAKE");
				if (theModel.takeablePieceExists(whosTurn)) {
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
				} else {
					changeTurn();
					showMessage(theModel.getOtherPlayer(whosTurn).isOwner() + " can't take any pieces." + whosTurn
							+ "'s turn.");
					checkIfMovePhase();
					checkIfJumpPhase();
				}
				break;
			case MOVE:
				System.out.println("MOVE");
				if (theModel.getSelectedNode() == null) {
					if (tmp.getOwner() == whosTurn) {
						tmp.setSelected(true);
						theModel.setSelectedNode(tmp);
						theModel.notifyDataSetChanged();
						showMessage(whosTurn + " has selected a piece for moving.");
					} else {
						showMessage(whosTurn + "! You cant move anything here. Please select another node.");
					}
				} else {
					if (tmp.getIndex() == theModel.getSelectedNode().getIndex()) {
						if (tmp.isSelected()) {
							tmp.setSelected(false);
							theModel.setSelectedNode(null);
							theModel.notifyDataSetChanged();
							showMessage(whosTurn
									+ " has de-selected a piece for moving. You can choose another piece now.");
						}
					} else {
						if (theModel.movePiece(theModel.getSelectedNode(), tmp)) {
							// piece moved successfully
							// de-select node
							// TODO: test if the next line is needed
							theModel.getSelectedNode().setSelected(false);
							theModel.setSelectedNode(null);
							theModel.notifyDataSetChanged();

							if (theModel.checkMills(tmp, whosTurn)) {
								// setting this piece has made a mill
								showMessage("Mill! " + whosTurn.toString()
										+ " can remove a piece of his opponent if possible.");
								lastState = currentState;
								currentState = GameState.TAKE;
							} else {
								// if the player created no mill with
								// this move,
								// its the other players turn
								// check and set gameState
								currentState = GameState.MOVE;
								changeTurn();
								showMessage(whosTurn + "'s turn. You can select a piece for moving.");
								checkIfJumpPhase();
							}
						} else {
							theModel.getSelectedNode().setSelected(false);
							theModel.setSelectedNode(null);
							theModel.notifyDataSetChanged();
							showMessage(whosTurn + "! You cant move here. Please select a new node for moving.");
						}
					}
				}
				break;
			case JUMP:
				if (theModel.getSelectedNode() == null) {
					if (tmp.getOwner() == whosTurn) {
						// if we haven't selected a node and the clicked
						// node belongs to us it gets selected
						tmp.setSelected(true);
						theModel.setSelectedNode(tmp);
						theModel.notifyDataSetChanged();
						showMessage(whosTurn + " has selected a node for jumping.");
					}
				} else {
					if (tmp.getIndex() == theModel.getSelectedNode().getIndex()) {
						// if we click on the already selected node, it
						// gets de-selected
						if (tmp.isSelected()) {
							theModel.getSelectedNode().setSelected(false);
							theModel.setSelectedNode(null);
							theModel.notifyDataSetChanged();
							showMessage(whosTurn + " has de-selected a node for jumping.");
						}
					} else if (theModel.jumpPiece(theModel.getSelectedNode(), tmp)) {
						// piece jumped successfully
						theModel.getSelectedNode().setSelected(false);
						theModel.setSelectedNode(null);
						theModel.notifyDataSetChanged();
						showMessage(whosTurn + " has jumped with a Piece.");

						if (theModel.checkMills(tmp, whosTurn)) {
							if (theModel.takeablePieceExists(whosTurn)) {
								lastState = currentState;
								currentState = GameState.TAKE;
								showMessage("Mill! " + whosTurn.toString() + " can remove a piece of his opponent.");
							} else {
								// if no mills
								changeTurn();
								checkIfMovePhase();
								checkIfJumpPhase();
							}
						} else {
							// if no mills
							changeTurn();
							checkIfMovePhase();
							checkIfJumpPhase();
						}
					} else {
						theModel.getSelectedNode().setSelected(false);
						theModel.setSelectedNode(null);
						theModel.notifyDataSetChanged();
						showMessage(whosTurn + "! You cant jump here. Please select another destination.");
					}
				}
				break;
			case GAMEOVER:
				// do nothing (rematch dialog)
				break;
			}

			// check if game is over
			if (checkForLose() != Owner.EMPTY) {
				currentState = GameState.GAMEOVER;
				showMessage("GAMEOVER! " + theModel.getOtherPlayer(whosTurn).isOwner() + " has won!");
				showRematchDialog();
			}
		}
	}

	public void showRematchDialog() {
		int input = JOptionPane.showOptionDialog(null, "Rematch?", "Rematch?", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, null, null);
		if (input == JOptionPane.OK_OPTION) {
			rematch();
		} else {
			System.exit(0); // exit program
		}
	}

	public void rematch() {
		theModel.rematch();
		lastState = null;
		currentState = GameState.SET;
		whosTurn = Owner.WHITE;
		theModel.notifyDataSetChanged();
		this.theView.messageView.setMessage(whosTurn + "'s turn. Set one of your pieces on the grid.");
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
				} else if (theModel.isPlayerBlocked(theModel.getPlayer(Owner.WHITE))) {
					return Owner.WHITE;
				} else if (theModel.isPlayerBlocked(theModel.getPlayer(Owner.BLACK))) {
					return Owner.BLACK;
				}
			}
		}
		return Owner.EMPTY;
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
		if (theModel.getPlayer(Owner.WHITE).getPiecesToSet() == 0
				&& theModel.getPlayer(Owner.BLACK).getPiecesToSet() == 0) {
			lastState = currentState;
			currentState = GameState.MOVE;
			showMessage(whosTurn + "'s turn. Move a piece.");
		}
	}

	/**
	 * changes whos turn it is depending on the current player
	 */
	public void changeTurn() {
		if (whosTurn == Owner.WHITE) {
			whosTurn = Owner.BLACK;
		} else {
			whosTurn = Owner.WHITE;
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o.getClass().toString().equals(MyView.class.toString())) {
			// click on MyView -> update Board/Model
			System.out.println("Observer tells that UI has been clicked");
			MouseEvent e = (MouseEvent) arg;
			processClick(e);
		} else if (o.getClass().toString().equals(Board.class.toString())) {
			// model has changed -> update MyView/View
			System.out.println("Observer tells that model has changed");
			theView.refresh((Map<String, Object>) arg);
		}
	}
}
