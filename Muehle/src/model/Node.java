package model;

import java.util.ArrayList;

import constants.Owner;

/**
 * This class represents a point on the board / grid.
 * 
 * @author Joris
 *
 */
public class Node {
	private int x;
	private int y;
	private int index; // see Board.java
	private boolean selected = false;
	private Owner owner; // which player has a piece on this node
	private ArrayList<Node> neighbors = new ArrayList<Node>(); // adjacent nodes

	Node(int x, int y, int index) {
		this.x = x;
		this.y = y;
		this.index = index;
		this.owner = Owner.EMPTY;
	}
	
	/**
	 * determines if the owner of this Node is no player / empty
	 * @return true or false
	 */
	public boolean belongsToNoPlayer() {
		return owner == Owner.EMPTY;
	}

	/**
	 * getter and setter
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isSelected() {
		return selected;
	}

	public void addNeighbor(Node node) {
		neighbors.add(node);
	}

	public ArrayList<Node> getNeighbors() {
		return neighbors;
	}

	public boolean hasOwner() {
		return owner != Owner.EMPTY;
	}

	public void setOwner(Owner owner) {

		this.owner = owner;
	}

	public Owner getOwner() {
		return owner;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
