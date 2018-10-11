package model;


import constants.Owner;

/**
 * This class represents the model of our Player, it extends Observable, so we can tell our views when to update
 * @author Joris
 *
 */
public class Player {
	private String name;
	private int score;
	private boolean isOnTurn;
	private Owner owner;
	private int piecesToSet = 9;
	private int piecesOnBoard = 0;
	
	public Player(String name) {
		this.name = name;
	}
	
	public void decrementPiecesToSet() {
		piecesToSet--;
		piecesOnBoard++;
	}
	
	public void decrementPiecesOnBoard() {
		piecesOnBoard--;
	}
	
	/**
	 * getters and setters
	 */
	
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public boolean isOnTurn() {
		return isOnTurn;
	}

	public void setOnTurn(boolean isOnTurn) {
		this.isOnTurn = isOnTurn;
	}

}
