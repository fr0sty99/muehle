package model;

import java.util.Observable;

import constants.Players;

public class Player extends Observable {
	private String name;
	private int score;
	private boolean isOnTurn;
	private Players owner;
	private int piecesToSet = 9;
	private int piecesOnBoard = 0;
	
	public Player(String name) {
		this.name = name;
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
		notifyObservers();
	}

	public Players isOwner() {
		return owner;
	}

	public void setOwner(Players owner) {
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
	
	public void decrementPiecesToSet() {
		piecesToSet--;
		piecesOnBoard++;
	}

}
