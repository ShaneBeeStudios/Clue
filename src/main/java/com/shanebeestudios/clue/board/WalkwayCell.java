/*
 * Class: WalkwayCell, child of BoardCell
 * Authors: Brandon Rodriguez, Hunter Lang
 * This class is used to keep track of the walkway cells in the game.
 * At the current moment is barebones, but has the boolean function "isWalkway" returning true and will be used to draw differently on the com.shanebeestudios.clue.gui
 */

package com.shanebeestudios.clue.board;

import java.awt.Color;
import java.awt.Graphics;

// WalkwayCell class body, extending BoardCell
public class WalkwayCell extends BoardCell {
		
	public WalkwayCell() {
		super();
		highlight = false;
	}
	
	// Overridden function from the parent class. Used to signify that this cell is a walkway
	@Override
	public boolean isWalkway() {
		return true;
	}

	@Override
	public void draw(Graphics g, Board b) {
		int pixelModifier = Math.min(b.size().width/b.getNumColumns(), b.size().height/b.getNumRows());
		//int pixelModifier = 25;
		g.setColor(Color.yellow);
		if (this.highlight) {
			g.setColor(Color.GREEN);
		}

		g.fillRect(column*pixelModifier, row*pixelModifier, pixelModifier, pixelModifier);
		g.setColor(Color.black);

		g.drawRect(column*pixelModifier, row*pixelModifier, pixelModifier, pixelModifier);
		
		
	}
	
	
	

}
