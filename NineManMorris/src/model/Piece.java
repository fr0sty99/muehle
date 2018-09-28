package model;

import constants.Players;

public class Piece {
	
	private Players belongsTo;
	private boolean isSelected = false;
	
	public Piece(Players belongsTo) {
		this.belongsTo = belongsTo;
	}
	
	public Piece(int index, Players belongsTo) {
		this.belongsTo = belongsTo;
	}
	
	public void setBelongsTo(Players belongsTo) {
		this.belongsTo = belongsTo;
	}
	public Players belongsTo() {
		return belongsTo;
	}
	
	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
}
