package model;

import constants.Owner;

/**
 * This class represents a nodeset on the board. This will be used for drawing
 * the grid and checking for mills, etc.
 * 
 * @author Joris
 *
 */
public class NodeSet {
	private Node[] nodes = new Node[3]; // a NodeSet always consists of 3 Nodes,
										// we have 16 nodeSets in total

	NodeSet(Node first) {
		nodes[0] = first;
	}

	NodeSet(Node first, Node second, Node third) {
		nodes[0] = first;
		nodes[1] = second;
		nodes[2] = third;
	}

	/**
	 * determines if the nodeSet contains a specific node
	 * 
	 * @param node
	 *            the node to look for
	 * @return if node is in this nodeSet
	 */
	boolean containsNode(Node node) {
		return nodes[0] == node || nodes[1] == node || nodes[2] == node;
	}

	/**
	 * determines if this nodeSet is a mill
	 * 
	 * @return which player owns the mill
	 */
	public Owner getPlayerIfMill() {
		if (getFirstNode().getOwner() == Owner.WHITE && getSecondNode().getOwner() == Owner.WHITE
				&& getThirdNode().getOwner() == Owner.WHITE) {
			return Owner.WHITE;
		}
		if (getFirstNode().getOwner() == Owner.BLACK && getSecondNode().getOwner() == Owner.BLACK
				&& getThirdNode().getOwner() == Owner.BLACK) {
			return Owner.BLACK;
		}
		return Owner.EMPTY;
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

}
