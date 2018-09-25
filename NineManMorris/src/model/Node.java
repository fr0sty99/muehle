package model;

import constants.Players;

public class Node {
	private int x;
	private int y;
	private int index;
	private Piece piece;
	
	public Node(int x, int y, int index) {
		this.x = x;
		this.y = y;
		this.index = index;
		this.piece = new Piece(Players.NOPLAYER);
	}
	
	public Node(int x, int y, int index, Players owner) {
		this.x = x;
		this.y = y;
		this.index = index;
		this.piece = new Piece(owner);
	}
	
	public void setPiece(Piece piece) {
		this.piece = piece;
	}
	
	public Piece getPiece() {
		return piece;
	}
	
	// returns true if no coin is on this node
	public boolean isEmpty() {
		return piece.belongsTo == Players.NOPLAYER;
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