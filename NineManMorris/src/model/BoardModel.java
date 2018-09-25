package model;

import java.util.ArrayList;
import java.util.Arrays;

import constants.Players;

public class BoardModel extends java.util.Observable {
// 	// index of nodeList works as following:
    //	0---------1---------2
	//	|		  |	    	|
	//	|  8------9------10 |
	//	|  |	  |      |  |
	//	|  |   16-17-18  |  |
	//  7-15--23	 19--11-3
	//  |  |   22-21-20  |  |
	//  |  |      |      |  |
	//  |  14----13-----12	|
	//	|		  |			|
	//	6---------5---------4

	
	private NodeSet[] nodeSets = new NodeSet[16]; // all possible mills 
	public NodeSet[] getNodeSets() {
		return nodeSets;
	}

	public void setNodeSets(NodeSet[] nodeSets) {
		this.nodeSets = nodeSets;
	}

	private Node[] grid = new Node[24]; // nodes
	private Player[] players = new Player[2];
	private ArrayList<Node> nodeList = new ArrayList<>(); // all nodes

	
	public BoardModel() {
		createNodeSets();
		createPlayers("Player1", "Player2"); // TODO: implement: Users should input names!
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
	
	public ArrayList<Node> getNodeList() {
		return nodeList;
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
	
	public void setPieceOnBoard(Piece piece) {
		grid[piece.boardIndex].setPiece(piece);
		notifyObservers(piece);
	}
	
	public Piece getPieceOnNodeFromIndex(int index) {
		return grid[index].getPiece();
	}

	public Node[] getNodes() {
		return grid;
	}
	
	public void createNodeSets() {
		int index = 0; // index of the nodes
		int dist = 3; // distance between nodes, this gets decremented after each iteration of the loop
		
		int nodeSetIndex = 0; // index of the nodeSets
		
		Node firstNode = null;
		
		for(int i = 0; i < 3; i++) { // used for the firstNodes coords
			// top NodeStet
			
			firstNode = new Node(i, i, index);
			index++;
			
			nodeList.add(firstNode);
			nodeSets[nodeSetIndex] = new NodeSet(firstNode);
			
			Node tmp = createNode(Direction.RIGHT, firstNode, index, dist);
			nodeList.add(tmp);
			index++;
			nodeSets[nodeSetIndex].setSecond(tmp);
			
			tmp = createNode(Direction.RIGHT, tmp, index, dist);
			nodeList.add(tmp);
			index++;
			nodeSets[nodeSetIndex].setThird(tmp);
			nodeSetIndex++;
			
			// right NodeSet
			nodeSets[nodeSetIndex] = new NodeSet(tmp); // first of this nodeSet is the last from the Last nodeSet
			
			tmp = createNode(Direction.BOTTOM, tmp, index, dist);
			nodeList.add(tmp);
			index++;
			nodeSets[nodeSetIndex].setSecond(tmp);
			
			tmp = createNode(Direction.BOTTOM, tmp, index, dist);
			nodeList.add(tmp);
			index++;
			nodeSets[nodeSetIndex].setThird(tmp);
			nodeSetIndex++;
			
			// bottom NodeSet
			nodeSets[nodeSetIndex] = new NodeSet(tmp); // first of this nodeSet is the last from the Last nodeSet
			
			tmp = createNode(Direction.LEFT, tmp, index, dist);
			nodeList.add(tmp);
			index++;
			nodeSets[nodeSetIndex].setSecond(tmp);
			
			tmp = createNode(Direction.LEFT, tmp, index, dist);
			nodeList.add(tmp);
			index++;
			nodeSets[nodeSetIndex].setThird(tmp);
			nodeSetIndex++;
			
			// left NodeSet
			nodeSets[nodeSetIndex] = new NodeSet(tmp);
			
			tmp = createNode(Direction.TOP, tmp, index, dist);
			nodeList.add(tmp);
			index++;
			nodeSets[nodeSetIndex].setSecond(tmp);
			
			tmp = firstNode;
			nodeSets[nodeSetIndex].setThird(tmp);
			nodeSetIndex++;
			
			// decrement dist because the triangles get smaller
			dist--;
		}
		
		// center nodeSets consist of already created nodes
		int firstNodeNodeSetIndex = 0;
		int secondtNodeNodeSetIndex = 4;
		int thirdNodeNodeSetIndex = 8;
		
		for(int i = 0; i < 4; i++) {
			nodeSets[nodeSetIndex] = new NodeSet(nodeSets[firstNodeNodeSetIndex].getSecondNode(), nodeSets[secondtNodeNodeSetIndex].getSecondNode(), nodeSets[thirdNodeNodeSetIndex].getSecondNode());
			nodeSetIndex++;
			firstNodeNodeSetIndex++;
			secondtNodeNodeSetIndex++;
			thirdNodeNodeSetIndex++;
		}
		
		System.out.println(Arrays.deepToString(nodeSets));
	}
	
	public Node createNode(Direction d, Node lastNode, int index, int dist) {
		switch(d) {
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
