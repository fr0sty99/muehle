package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observer;

import constants.Direction;
import constants.Owner;

/**
 * This class represents our model for the Board
 * 
 * @author Joris Neber
 */
public class Board extends java.util.Observable {
	// // index of nodes works as following:
	// 0---------1---------2
	// | ........|.........|
	// | 8-------9------10 |
	// | | ......| ......| |
	// | | ..16-17-18   .|.|
	// 7-15--23 ....9--11--3
	// | | ..22-21-20. .| .|
	// | | ......|   ...|. |
	// | 14-----13-----12 .|
	// | ........| ........|
	// 6---------5---------4

	private NodeSet[] nodeSets = new NodeSet[16];
	private Node selectedNode;
	private Player[] players = new Player[2];

	/**
	 * constructor initializes nodeSets and player
	 */
	public Board(Observer observer) {
		addObserver(observer);

		createPlayers("Player1", "Player2");
		createNodeSets();
	}

	/**
	 * creates the players
	 * 
	 * @param name1
	 *            the name of player1
	 * @param name2
	 *            the name of player2
	 */
	private void createPlayers(String name1, String name2) {
		players[0] = new Player(Owner.WHITE);

		players[1] = new Player(Owner.BLACK);
	}

	/**
	 * creates the nodes in nodeSets (which also are representing the mills)
	 */
	private void createNodeSets() {

		// our board has 3 different-sized rectangles which consist of four
		// NodeSets each (12 in total).
		// In addition to that we have four more NodeSets which point to the
		// center. they are connecting the "rectangles"

		int index = 0; // index of the nodes
		int dist = 3;
		// distance between nodes, used for creating the coords. this gets
		// decremented after each iteration of the loop, as the rectangles gets
		// smaller

		// for better imagination, imagine a board with coordinates behind the
		// grid like this:

		// (X, Y) representing the coords.
		// for example index 8 is (1,1) and index 21 is (4,3)
		//
		// (NODE)(0,1)(0,2)(NODE)(0,4)(0,5)(NODE)
		// (1,0)(NODE)(1,2)(NODE)(1,4)(NODE)(1,6)
		// (2,0)(2,1)(NODE)(NODE)(NODE)(2,5)(2,6)
		// (NODE)(NODE)(NODE)(3,3)(NODE)(NODE)(NODE)
		// (4,0)(4,1)(NODE)(NODE)(NODE)(4,5)(4,6)
		// (5,0)(NODE)(5,2)(NODE)(5,4)(NODE)(5,6)
		// (NODE)(6,1)(6,2)(NODE)(6,4)(6,5)(NODE)

		int nodeSetIndex = 0; // index of the nodeSets

		Node firstNode = null; // we need to save the first node of each
								// "rectangle"-iteration
		// which is the one in the upper left corner of each "rectangle"
		// because it is in the first but also in the last nodeSet of a
		// "rectangle"

		for (int i = 0; i < 3; i++) {
			// we loop 3 times for 3 rectangles

			// create the first Node, which represents our starting point
			firstNode = new Node(i, i, index);

			// top NodeStet
			nodeSets[nodeSetIndex] = new NodeSet(firstNode);
			index++;

			// create the second node
			Node tmp = createNode(Direction.RIGHT, firstNode, index, dist);
			nodeSets[nodeSetIndex].setSecond(tmp);
			addNeighbors(tmp, firstNode);
			index++;

			// create the third node
			Node secondTmp = createNode(Direction.RIGHT, tmp, index, dist);
			nodeSets[nodeSetIndex].setThird(secondTmp);
			addNeighbors(tmp, secondTmp);
			index++;
			nodeSetIndex++;

			// right NodeSet
			// the first node of this nodeSet is the last node from the last
			// NodeSet
			nodeSets[nodeSetIndex] = new NodeSet(secondTmp);

			// create second node of set
			tmp = createNode(Direction.BOTTOM, secondTmp, index, dist);
			nodeSets[nodeSetIndex].setSecond(tmp);
			addNeighbors(tmp, secondTmp);
			index++;

			// create third node of set
			secondTmp = createNode(Direction.BOTTOM, tmp, index, dist);
			nodeSets[nodeSetIndex].setThird(secondTmp);
			addNeighbors(tmp, secondTmp);
			index++;
			nodeSetIndex++;

			// bottom NodeSet
			// the first node of this nodeSet is the last node from the last
			// NodeSet
			nodeSets[nodeSetIndex] = new NodeSet(secondTmp);

			// create second node of set
			tmp = createNode(Direction.LEFT, secondTmp, index, dist);
			nodeSets[nodeSetIndex].setSecond(tmp);
			addNeighbors(tmp, secondTmp);
			index++;

			// create third node of set
			secondTmp = createNode(Direction.LEFT, tmp, index, dist);
			addNeighbors(tmp, secondTmp);
			nodeSets[nodeSetIndex].setThird(secondTmp);
			index++;
			nodeSetIndex++;

			// left NodeSet
			// the first node of this nodeSet is the last node from the last
			// NodeSet
			nodeSets[nodeSetIndex] = new NodeSet(secondTmp);

			// create second node of set
			tmp = createNode(Direction.TOP, secondTmp, index, dist);
			addNeighbors(tmp, secondTmp);
			nodeSets[nodeSetIndex].setSecond(tmp);
			index++;

			// the third node of this nodeSet is firstNode
			nodeSets[nodeSetIndex].setThird(firstNode);
			nodeSetIndex++;

			// add neighbors
			addNeighbors(tmp, firstNode);

			// decrement dist, because the next rectangle is smaller
			dist--;
		}

		// center nodeSets consisting of already created nodes
		int firstNodeNodeSetIndex = 0, secondtNodeNodeSetIndex = 4, thirdNodeNodeSetIndex = 8; // starting
																								// indexes
		for (int i = 0; i < 4; i++) {
			nodeSets[nodeSetIndex] = new NodeSet(nodeSets[firstNodeNodeSetIndex].getSecondNode(),
					nodeSets[secondtNodeNodeSetIndex].getSecondNode(), nodeSets[thirdNodeNodeSetIndex].getSecondNode());
			nodeSetIndex++;
			firstNodeNodeSetIndex++;
			secondtNodeNodeSetIndex++;
			thirdNodeNodeSetIndex++;
		}

		// center neighbors
		int a = 0, b = 4, c = 8; // starting indexes
		for (int i = 0; i < 4; i++) {
			addNeighbors(nodeSets[a].getSecondNode(), nodeSets[b].getSecondNode());
			addNeighbors(nodeSets[b].getSecondNode(), nodeSets[c].getSecondNode());
			a++;
			b++;
			c++;
		}

		System.out.println("Finished creating nodeSets");

		notifyDataSetChanged();
	}

	/**
	 * removes a piece from nodeSets if its allowed and return if it was removed
	 * successfully
	 * 
	 * @param index
	 *            the index of the node to be removed
	 * @return if the node has been removed successfully
	 */
	private boolean removePieceFromNodeSets(int index) {
		for (NodeSet set : nodeSets) {
			if (set.getFirstNode().getIndex() == index) {
				set.getFirstNode().setOwner(Owner.EMPTY);
				notifyDataSetChanged();
				return true;
			}
			if (set.getSecondNode().getIndex() == index) {
				set.getSecondNode().setOwner(Owner.EMPTY);
				notifyDataSetChanged();
				return true;
			}
			if (set.getThirdNode().getIndex() == index) {
				set.getThirdNode().setOwner(Owner.EMPTY);
				notifyDataSetChanged();
				return true;
			}
		}
		return false;
	}

	/**
	 * creates a new Node in the given direction in the given distance (coord
	 * system)
	 *
	 * @param d
	 *            the direction where we want to create our node
	 * @param lastNode
	 *            the lastNode from where we want to create the new Node
	 * @param index
	 *            the index of the new Node
	 * @param dist
	 *            the distance we go in the given direction "d"
	 * @return new Node
	 */
	private Node createNode(Direction d, Node lastNode, int index, int dist) {
		switch (d) {
		case TOP:
			return new Node(lastNode.getX(), lastNode.getY() - dist, index);
		case RIGHT:
			return new Node(lastNode.getX() + dist, lastNode.getY(), index);
		case BOTTOM:
			return new Node(lastNode.getX(), lastNode.getY() + dist, index);
		case LEFT:
			return new Node(lastNode.getX() - dist, lastNode.getY(), index);
		}

		System.out.println("GameController::createNode() --- could not create node");
		return null; // could not create node
	}

	/**
	 * finds and returns a node by a given index
	 * 
	 * @param index
	 *            the index of the node we want
	 * @return the node with the given index
	 */
	private Node getNodeFromIndex(int index) {
		for (NodeSet set : nodeSets) {
			if (set.getFirstNode().getIndex() == index) {
				return set.getFirstNode();
			}
			if (set.getSecondNode().getIndex() == index) {
				return set.getSecondNode();
			}
			if (set.getThirdNode().getIndex() == index) {
				return set.getThirdNode();
			}
		}
		return null;
	}

	/**
	 * adds each Node to the others neighborList
	 * 
	 * @param one
	 *            the first node
	 * @param two
	 *            the second Node
	 */
	private void addNeighbors(Node one, Node two) {
		one.addNeighbor(two);
		two.addNeighbor(one);
	}

	/**
	 * sets a piece at the given index and determines if it was successfully
	 * 
	 * @param index
	 *            index of the piece
	 * @param owner
	 *            owner of the piece (player)
	 * @return if setting the piece was allowed and successfull
	 */
	public boolean setPiece(int index, Owner owner) {
		for (NodeSet set : nodeSets) {
			if (set.getFirstNode().getIndex() == index && set.getFirstNode().getOwner() == Owner.EMPTY) {
				set.getFirstNode().setOwner(owner);
				getPlayer(owner).decrementPiecesToSet();
				notifyDataSetChanged();
				return true;
			}
			if (set.getSecondNode().getIndex() == index && set.getSecondNode().getOwner() == Owner.EMPTY) {
				set.getSecondNode().setOwner(owner);
				getPlayer(owner).decrementPiecesToSet();
				notifyDataSetChanged();
				return true;
			}
			if (set.getThirdNode().getIndex() == index && set.getThirdNode().getOwner() == Owner.EMPTY) {
				set.getThirdNode().setOwner(owner);
				getPlayer(owner).decrementPiecesToSet();
				notifyDataSetChanged();
				return true;
			}
		}
		return false;
	}

	/**
	 * determines if taking a piece is possible and does that if yes
	 * 
	 * @param index
	 *            the index of the piece to be taken
	 * @param owner
	 *            the player who wants to take the piece
	 * @return if taking the piece was successfull
	 */
	public boolean takePiece(int index, Owner owner) {
		if (getNodeFromIndex(index).getOwner() != Owner.EMPTY && getNodeFromIndex(index).getOwner() != owner
				&& !checkMills(getNodeFromIndex(index), getNodeFromIndex(index).getOwner())) {
			getOtherPlayer(owner).decrementPiecesOnBoard();
			return removePieceFromNodeSets(index);
		}
		return false;
	}

	/**
	 * determines if jumping a piece is possible and does that if yes0
	 * 
	 * @param start
	 *            the sartNode of the move
	 * @param dest
	 *            the destinationNode of the move
	 * @return if the jump was allowed and successful
	 */
	public boolean jumpPiece(Node start, Node dest) {
		if (dest.getOwner() == Owner.EMPTY) {
			getNodeFromIndex(dest.getIndex()).setOwner(start.getOwner());
			getNodeFromIndex(start.getIndex()).setOwner(Owner.EMPTY);
			notifyDataSetChanged();
			return true;
		}
		return false;
	}

	/**
	 * determines if moving a piece is possible and does that if yes
	 * 
	 * @param start
	 *            the sartNode of the move
	 * @param dest
	 *            the destinationNode of the move
	 * @return if the move was allowed and successful
	 */
	public boolean movePiece(Node start, Node dest) {
		if (dest.getOwner() == Owner.EMPTY) {
			if (start.getNeighbors().contains(dest)) {
				getNodeFromIndex(dest.getIndex()).setOwner(start.getOwner());
				getNodeFromIndex(start.getIndex()).setOwner(Owner.EMPTY);
				notifyDataSetChanged();
				return true;
			}
		}
		return false;
	}

	/**
	 * checks the NodeSet which holds the node for a mill
	 * 
	 * @param node
	 *            the node in the NodeSet which possibly is a mill
	 * @param owner
	 *            the player which owns the node
	 * @return if there is a mill or not
	 */
	public boolean checkMills(Node node, Owner owner) {
		for (NodeSet set : nodeSets) {
			if (set.getPlayerIfMill() == owner && set.containsNode(node)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * determines whether a player is able to move or not CREDITS:
	 * https://github.com/theoriginalbit
	 * 
	 * @param player
	 *            the player to check
	 * @return if there is any node which has a free node as neighbor
	 */
	public boolean isPlayerBlocked(Player player) {
		int totalPieces = 0, totalBlocked = 0;

		for (NodeSet set : nodeSets) {
			int neighborCount = 0;
			for (Node node : set.getNodes()) {
				// for all nodes
				if (node.getOwner() == player.isOwner()) {
					// if the given player is the owner of the node
					ArrayList<Node> neighbors = node.getNeighbors();
					for (Node neighbor : neighbors) {
						if (!neighbor.belongsToNoPlayer()) {
							++neighborCount;
						}
					}

					// if there is no free neighbor around this noce, this piece
					// is blocked
					if (neighborCount == neighbors.size()) {
						++totalBlocked;
					}

					totalPieces++;
				}
			}
		}

		// if all the players pieces are blocked, the player cant move
		return totalPieces == totalBlocked;
	}

	/**
	 * determines wether a player can take any pieces or not (maybe all of them
	 * are in mills)
	 * 
	 * @param player
	 *            the asking player
	 * @return if a player can take a piece
	 */
	public boolean takeablePieceExists(Owner askingPlayer) {
		// the opponent of the current player
		Owner otherPlayer = getOtherPlayer(askingPlayer).isOwner();

		// for every nodeSet
		for (NodeSet set : nodeSets) {
			for (Node node : set.getNodes()) {
				if (node.getOwner() == otherPlayer) {
					if (!checkMills(node, node.getOwner())) {

						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * returns the opposite player of a given owner
	 * 
	 * @param notThisOne
	 *            we want the other player than this one
	 * @return the other player
	 */
	public Player getOtherPlayer(Owner notThisOne) {
		if (notThisOne == Owner.WHITE) {
			return players[1];
		} else {
			return players[0];
		}
	}

	/**
	 * prepares the data of this class for a rematch
	 */
	public void rematch() {
		nodeSets = new NodeSet[16];
		players = new Player[2];
		createNodeSets();
		createPlayers("Player1", "Player2");
	}

	/**
	 * notifies our Observers(View) that our dataSet has changed
	 */
	public void notifyDataSetChanged() {
		// notify that our data has changed
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("nodeSets", nodeSets);
		data.put("players", players);
		setChanged();
		notifyObservers(data);
	}

	/**
	 * Getters and Setters
	 */
	public NodeSet[] getNodeSets() {
		return nodeSets;
	}

	public void setNodeSets(NodeSet[] nodeSets) {
		this.nodeSets = nodeSets;
	}

	public void setSelectedNode(Node node) {
		this.selectedNode = node;
	}

	public Node getSelectedNode() {
		return selectedNode;
	}

	public Player[] getPlayers() {
		return players;
	}

	public Player getPlayer(Owner owner) {
		if (owner == Owner.WHITE) {
			return players[0];
		} else {
			return players[1];
		}
	}

}
