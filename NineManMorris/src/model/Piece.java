package model;

import java.awt.Graphics;

import constants.Owner;

public class Piece {
	
	public int index;
	public Owner belongsTo;
	
	public Piece(int index, Owner belongsTo) {
		this.index = index;
		this.belongsTo = belongsTo;
	}
	
	public void draw(Graphics g) {
		
	}
}
