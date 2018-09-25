package model;

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
	
	public void setSecond(Node node) {
		nodes[1] = node;
	}
	
	public void setThird(Node node) {
		nodes[2] = node;
	}
	
	public Node getFirstNode() {
		return nodes[0];
	}
	
	public Node getSecondNode() {
		return nodes[1];
	}
	
	public Node getThirdNode() {
		return nodes[2];
	}
	
	
}
