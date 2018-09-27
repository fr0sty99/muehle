package model;

import constants.Players;

// always consists of 3 Nodes, we have 20 nodeSets in total
public class NodeSet {
	public Node[] nodes = new Node[3];

	public NodeSet(Node first) {
		nodes[0] = first;
	}

	public NodeSet(Node first, Node second, Node third) {
		nodes[0] = first;
		nodes[1] = second;
		nodes[2] = third;
	}
	
	public void setFirst(Node node) {
		nodes[0] = node;
	}

	public void setSecond(Node node) {
		nodes[1] = node;
	}

	public void setThird(Node node) {
		nodes[2] = node;
	}

	public Node getFirstNode() {
		return nodes[0];
	}
	
	public Node[] getNodes() {
		return nodes;
	}

	public Node getSecondNode() {
		return nodes[1];
	}

	public Node getThirdNode() {
		return nodes[2];
	}
	
	public boolean containsNode(Node node) {
		return nodes[0] == node || nodes[1] == node || nodes[2] == node;
	}
	
	public Players hasMillFromPlayer() {
		if (getFirstNode().getPiece().belongsTo == Players.PLAYER1
				&& getSecondNode().getPiece().belongsTo == Players.PLAYER1
				&& getThirdNode().getPiece().belongsTo == Players.PLAYER1) {
			return Players.PLAYER1;
		}
		if (getFirstNode().getPiece().belongsTo == Players.PLAYER2
				&& getSecondNode().getPiece().belongsTo == Players.PLAYER2
				&& getThirdNode().getPiece().belongsTo == Players.PLAYER2) {
			return Players.PLAYER2;
		}
		return Players.NOPLAYER;
	}

}
