package model;

import constants.Players;

public class Piece {
	
	public int boardIndex;// 99 means it's not set
	public Players belongsTo;
	
	public Piece(Players belongsTo) {
		this.belongsTo = belongsTo;
	}
	
	public Piece(int index, Players belongsTo) {
		this.boardIndex = index;
		this.belongsTo = belongsTo;
	}
	
}
