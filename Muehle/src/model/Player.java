package model;

import constants.Owner;

/**
 * This class represents the model of our Player, it extends Observable, so we
 * can tell our views when to update
 * 
 * @author Joris
 *
 */
public class Player {
	private boolean isOnTurn;
	private Owner owner;
	private int piecesToSet = 9;
	private int piecesOnBoard = 0;

	Player(Owner owner) {
		this.owner = owner;
	}

	void decrementPiecesToSet() {
		piecesToSet--;
		piecesOnBoard++;
	}

	void decrementPiecesOnBoard() {
		piecesOnBoard--;
	}

	/**
	 * getters and setters
	 */
	
	public Owner getOwner() {
		return owner;
	}
	
	

	public int getPiecesOnBoard() {
		return piecesOnBoard;
	}

	public void setPiecesOnBoard(int piecesOnBoard) {
		this.piecesOnBoard = piecesOnBoard;
	}

	public int getPiecesToSet() {
		return piecesToSet;
	}

	public void setPiecesToSet(int piecesToSet) {
		this.piecesToSet = piecesToSet;
	}

	public Owner isOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	public boolean isOnTurn() {
		return isOnTurn;
	}

	public void setOnTurn(boolean isOnTurn) {
		this.isOnTurn = isOnTurn;
	}

}
