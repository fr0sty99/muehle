package model;

import constants.Direction;
import constants.Owner;

/**
 * This class represents our model for the Board
 * @author Joris Neber
 */
public class BoardModel extends java.util.Observable {
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
	public BoardModel() {
		createNodeSets();
		createPlayers("Player1", "Player2");
	}

	/** 
	 * creaates the players
	 * @param name1 the name of player1
	 * @param name2 the name of player2
	 */
	public void createPlayers(String name1, String name2) {
		players[0] = new Player(name1);
		players[0].setOwner(Owner.PLAYER1);

		players[1] = new Player(name2);
		players[1].setOwner(Owner.PLAYER2);
		notifyObservers(players);
	}

	/**
	 * determines if taking a piece is possible and does that if yes
	 * @param index the index of the piece to be taken
	 * @param owner the player who wants to take the piece
	 * @return if taking the piece was successfull
	 */
	public boolean takePiece(int index, Owner owner) {
		if (getNode(index).getOwner() != Owner.NOPLAYER && getNode(index).getOwner() != owner
				&& nodeisInNoMill(getNode(index))) {
			getOtherPlayer(owner).decrementPiecesOnBoard();
			return removePieceFromNodeSets(index);
		}
		return false;
	}

	/** 
	 * determines if jumping a piece is possible and does that if yes0
	 * @param start the sartNode of the move
	 * @param dest the destinationNode of the move
	 * @return if the jump was allowed and successful
	 */
	public boolean jumpPiece(Node start, Node dest) {
		if (dest.getOwner() == Owner.NOPLAYER) {
			getNode(dest.getIndex()).setOwner(start.getOwner());
			getNode(start.getIndex()).setOwner(Owner.NOPLAYER);
			notifyDataSetChanged();
			return true;
		}
		return false;
	}

	/**
	 * determines if moving a piece is possible and does that if yes
	 * @param start the sartNode of the move
	 * @param dest the destinationNode of the move
	 * @return if the move was allowed and successful
	 */
	public boolean movePiece(Node start, Node dest) {
		if (dest.getOwner() == Owner.NOPLAYER) {
			if (start.getNeighbors().contains(dest)) {
				getNode(dest.getIndex()).setOwner(start.getOwner());
				getNode(start.getIndex()).setOwner(Owner.NOPLAYER);
				notifyDataSetChanged();
				return true;
			}
		}
		return false;
	}

	/**
	 * sets a new node at the given index and determines if it was successfully
	 * @param index index of the new node
	 * @param owner owner of the new node (player)
	 * @return if setting the node was allowed and successfull
	 */
	public boolean setNode(int index, Owner owner) {
		for (NodeSet set : nodeSets) {
			if (set.getFirstNode().getIndex() == index && set.getFirstNode().getOwner() == Owner.NOPLAYER) {
				set.getFirstNode().setOwner(owner);
				getPlayer(owner).decrementPiecesToSet();
				notifyDataSetChanged();
				return true;
			}
			if (set.getSecondNode().getIndex() == index && set.getSecondNode().getOwner() == Owner.NOPLAYER) {
				set.getSecondNode().setOwner(owner);
				getPlayer(owner).decrementPiecesToSet();
				notifyDataSetChanged();
				return true;
			}
			if (set.getThirdNode().getIndex() == index && set.getThirdNode().getOwner() == Owner.NOPLAYER) {
				set.getThirdNode().setOwner(owner);
				getPlayer(owner).decrementPiecesToSet();
				notifyDataSetChanged();
				return true;
			}
		}
		return false;
	}

	/**
	 * notifies our Observers(View) that our dataSet has changed
	 */
	public void notifyDataSetChanged() {
		// notify that our data has changed
		setChanged();
		notifyObservers(this);
	}

	/**
	 *	removes a piece from nodeSets if its allowed and return if it was removed successfully
	 * @param index the index of the node to be removed
	 * @return if the node has been removed successfully
	 */
	public boolean removePieceFromNodeSets(int index) {
		for (NodeSet set : nodeSets) {
			if (set.getFirstNode().getIndex() == index) {
				set.getFirstNode().setOwner(Owner.NOPLAYER);
				notifyDataSetChanged();
				return true;
			}
			if (set.getSecondNode().getIndex() == index) {
				set.getSecondNode().setOwner(Owner.NOPLAYER);
				notifyDataSetChanged();
				return true;
			}
			if (set.getThirdNode().getIndex() == index) {
				set.getThirdNode().setOwner(Owner.NOPLAYER);
				notifyDataSetChanged();
				return true;
			}
		}
		return false;
	}

	/**
	 * determines if a piece is in a mill or not
	 * @param node the node to check 
	 * @return if node is not in a mill
	 */
	public boolean nodeisInNoMill(Node node) {
		if (checkMills(node, node.getOwner())) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * creates the nodes in nodeSets (which also are representing the mills)
	 */
	public void createNodeSets() {
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
	}

	/**
	 * adds each Node to the others neighborList
	 * 
	 * @param one
	 *            the first node
	 * @param two
	 *            the second Node
	 */
	public void addNeighbors(Node one, Node two) {
		one.addNeighbor(two);
		two.addNeighbor(one);
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
			if (set.hasMillFromPlayer() == owner && set.containsNode(node)) {
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
	public Node createNode(Direction d, Node lastNode, int index, int dist) {
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

	public NodeSet getParentNodeSet(Node node) {
		for (NodeSet set : nodeSets) {
			if (set.containsNode(node)) {
				return set;
			}
		}
		return null;
	}

	public Node getNode(int index) {
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

	public void setPlayer(Owner owner, String name) {
		if (owner == Owner.PLAYER1) {
			players[0] = new Player(name);
		} else {
			players[1] = new Player(name);
		}
	}

	public Player getPlayer(Owner owner) {
		if (owner == Owner.PLAYER1) {
			return players[0];
		} else {
			return players[1];
		}
	}

	public Player getOtherPlayer(Owner notThisOne) {
		if (notThisOne == Owner.PLAYER1) {
			return players[1];
		} else {
			return players[0];
		}
	}
}
