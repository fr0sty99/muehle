package model;

import java.util.Arrays;

import constants.Players;

public class BoardModel extends java.util.Observable {
	// // index of nodeList works as following:
	// 0---------1---------2
	// | | |
	// | 8-------9------10 |
	// | | | | |
	// | | 16-17-18   | |
	// 7-15--23 9--11--3
	// | | 22-21-20 | |
	// | | |   | |
	// | 14-----13-----12 |
	// | | |
	// 6---------5---------4

	private NodeSet[] nodeSets = new NodeSet[16]; // all possible mills, needed
													// for drawing the grid
													// easily

	public NodeSet[] getNodeSets() {
		return nodeSets;
	}

	public void setNodeSets(NodeSet[] nodeSets) {
		this.nodeSets = nodeSets;
	}

	private Node selectedNode;
	private Player[] players = new Player[2];

	public BoardModel() {
		createNodeSets();
		createPlayers("Player1", "Player2");
		// TODO: implement: Users should input names in a dialog
	}

	public void setSelectedNode(Node node) {
		this.selectedNode = node;
	}

	public Node getSelectedNode() {
		return selectedNode;
	}

	public void createPlayers(String name1, String name2) {
		players[0] = new Player(name1);
		players[0].setOwner(Players.PLAYER1);

		players[1] = new Player(name2);
		players[1].setOwner(Players.PLAYER2);

		notifyObservers(players);
	}

	public Player[] getPlayers() {
		return players;
	}

	public void setPlayer(Players owner, String name) {
		if (owner == Players.PLAYER1) {
			players[0] = new Player(name);
		} else {
			players[1] = new Player(name);
		}
	}

	public Player getPlayer(Players owner) {
		if (owner == Players.PLAYER1) {
			return players[0];
		} else {
			return players[1];
		}
	}

	public boolean takePiece(int index, Players owner) {
		if (getPieceFromNodeSets(index).belongsTo() != Players.NOPLAYER
				&& getPieceFromNodeSets(index).belongsTo() != owner) {
			notifyDataSetChanged();
			return removePieceFromNodeSets(index);
		}
		return false;
	}

	public boolean movePiece(Node start, Node dest) {
		if(dest.getPiece().belongsTo() == Players.NOPLAYER) {
			if(start.getNeighbors().contains(dest)) {
				getPieceFromNodeSets(dest.getIndex()).setBelongsTo(start.getPiece().belongsTo());
				getPieceFromNodeSets(start.getIndex()).setBelongsTo(Players.NOPLAYER);
				notifyDataSetChanged();
				return true;
			}
		}
		return false;
	}

	public boolean setPieceToNodeSet(int index, Players owner) {
		for (NodeSet set : nodeSets) {
			if (set.getFirstNode().getIndex() == index && set.getFirstNode().getPiece().belongsTo() == Players.NOPLAYER) {
				set.getFirstNode().setPiece(new Piece(owner));
				getPlayer(owner).decrementPiecesToSet();
				notifyDataSetChanged();
				return true;
			}
			if (set.getSecondNode().getIndex() == index
					&& set.getSecondNode().getPiece().belongsTo() == Players.NOPLAYER) {
				set.getSecondNode().setPiece(new Piece(owner));
				getPlayer(owner).decrementPiecesToSet();
				notifyDataSetChanged();
				return true;
			}
			if (set.getThirdNode().getIndex() == index && set.getThirdNode().getPiece().belongsTo() == Players.NOPLAYER) {
				set.getThirdNode().setPiece(new Piece(owner));
				getPlayer(owner).decrementPiecesToSet();
				notifyDataSetChanged();
				return true;
			}
		}
		return false;
	}

	public void notifyDataSetChanged() {
		// notify that our data has changed
		setChanged();
		notifyObservers(this);
	}

	public Piece getPieceFromNodeSets(int index) {
		for (NodeSet set : nodeSets) {
			if (set.getFirstNode().getIndex() == index) {
				return set.getFirstNode().getPiece();
			}
			if (set.getSecondNode().getIndex() == index) {
				return set.getSecondNode().getPiece();
			}
			if (set.getThirdNode().getIndex() == index) {
				return set.getThirdNode().getPiece();
			}
		}
		return null;
	}

	public boolean removePieceFromNodeSets(int index) {
		for (NodeSet set : nodeSets) {
			if (set.getFirstNode().getIndex() == index) {
				set.getFirstNode().setPiece(new Piece(Players.NOPLAYER));
				notifyDataSetChanged();
				return true;
			}
			if (set.getSecondNode().getIndex() == index) {
				set.getSecondNode().setPiece(new Piece(Players.NOPLAYER));
				notifyDataSetChanged();
				return true;
			}
			if (set.getThirdNode().getIndex() == index) {
				set.getThirdNode().setPiece(new Piece(Players.NOPLAYER));
				notifyDataSetChanged();
				return true;
			}
		}
		return false;
	}

	public void createNodeSets() {
		// creating the nodes in nodeSets ( which also represents a mill )
		// our board has 3 different-sized rectangles which consist of four
		// NodeSets each (12 in total).
		// In Addition to that we have four more NodeSets which connect the
		// "rectangles"

		int index = 0; // index of the nodes

		// INDEX gets set like this
		// 0---------1---------2
		// | | |
		// | 8-------9------10 |
		// | | | | |
		// | | 16-17-18   | |
		// 7-15--23 9--11--3
		// | | 22-21-20 | |
		// | | |   | |
		// | 14-----13-----12 |
		// | | |
		// 6---------5---------4

		int dist = 3; // distance between nodes, used for creating the coords.
						// this gets decremented after
						// each iteration of the loop, as the rectangles get
						// smaller

		// for better imagination, imagine a board with coordinates behind the
		// grid like this:

		// (X, Y) representing the coords.
		//
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

		for (int i = 0; i < 3; i++) { // we loop 3 times for 3 rectangles
			// top NodeStet

			// create the first Node
			firstNode = new Node(i, i, index);
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

			// create second node
			tmp = createNode(Direction.BOTTOM, secondTmp, index, dist);
			nodeSets[nodeSetIndex].setSecond(tmp);
			addNeighbors(tmp, secondTmp);
			index++;
			

			// create third node
			secondTmp = createNode(Direction.BOTTOM, tmp, index, dist);
			nodeSets[nodeSetIndex].setThird(secondTmp);
			addNeighbors(tmp, secondTmp);
			index++;
			nodeSetIndex++;

			// bottom NodeSet
			// the first node of this nodeSet is the last node from the last
			// NodeSet
			nodeSets[nodeSetIndex] = new NodeSet(secondTmp);

			tmp = createNode(Direction.LEFT, secondTmp, index, dist);
			nodeSets[nodeSetIndex].setSecond(tmp);
			addNeighbors(tmp, secondTmp);
			index++;

			secondTmp = createNode(Direction.LEFT, tmp, index, dist);
			addNeighbors(tmp, secondTmp);
			nodeSets[nodeSetIndex].setThird(secondTmp);
			index++;
			nodeSetIndex++;

			// left NodeSet
			// the first node of this nodeSet is the last node from the last
			// NodeSet
			nodeSets[nodeSetIndex] = new NodeSet(secondTmp);

			tmp = createNode(Direction.TOP, secondTmp, index, dist);
			addNeighbors(tmp, secondTmp);
			nodeSets[nodeSetIndex].setSecond(tmp);
			index++;

			// the first node of this nodeSet is the firstNode
			nodeSets[nodeSetIndex].setThird(firstNode);
			nodeSetIndex++;

			// add neighbors
			addNeighbors(tmp, firstNode);

			// decrement dist, because the next rectangle is smaller
			dist--;
		}

		// center nodeSets consisting of already created nodes
		int firstNodeNodeSetIndex = 0;
		int secondtNodeNodeSetIndex = 4;
		int thirdNodeNodeSetIndex = 8;

		for (int i = 0; i < 4; i++) {
			nodeSets[nodeSetIndex] = new NodeSet(nodeSets[firstNodeNodeSetIndex].getSecondNode(),
					nodeSets[secondtNodeNodeSetIndex].getSecondNode(), nodeSets[thirdNodeNodeSetIndex].getSecondNode());

			nodeSetIndex++;
			firstNodeNodeSetIndex++;
			secondtNodeNodeSetIndex++;
			thirdNodeNodeSetIndex++;
		}

		// center neighbors
		int a = 0, b = 4, c = 8;
		for (int i = 0; i < 4; i++) {
			
			addNeighbors(nodeSets[a].getSecondNode(), nodeSets[b].getSecondNode());
			addNeighbors(nodeSets[b].getSecondNode(), nodeSets[c].getSecondNode());

			a++;
			b++;
			c++;
		}

		System.out.println(Arrays.deepToString(nodeSets));
	}
	
	/**
	 * 	adds each Node to the others neighborList
	 * 
	 * @param one the first node
	 * @param two the second Node
	 */
	public void addNeighbors(Node one, Node two) {
		one.addNeighbor(two);
		two.addNeighbor(one);
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

}
