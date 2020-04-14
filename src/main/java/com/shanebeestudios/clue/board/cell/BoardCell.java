/*
 * ABSTRACT Class: BoardCell, parent of WalkwayCell and RoomCell
 * Authors: Brandon Rodriguez, Hunter Lang
 * This class provides the framework needed for the different types of cells in the game
 */
package com.shanebeestudios.clue.board.cell;

import com.shanebeestudios.clue.board.Board;
import com.shanebeestudios.clue.game.Room;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public abstract class BoardCell {

    protected int row;
    protected int column;
    protected boolean highlight;
    protected final Room room;

    public BoardCell(Room room) {
        this.room = room;
    }

    // "is" functions are set to false then overridden in the appropriate cell to suit that cell's needs
    public boolean isWalkway() {
        return false;
    }

    public boolean isRoom() {
        return false;
    }

    public boolean isDoorway() {
        return false;
    }

    public boolean isOutside() {
        return false;
    }

    public boolean isHighlight() {
        return highlight;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    @SuppressWarnings("deprecation")
    public boolean containsClick(int x, int y, Board b) {
        int pixelModifier = Math.min(b.size().width / b.getNumColumns(), b.size().height / b.getNumRows());
        //Rectangle rect = new Rectangle(column*25, row*25, 25, 25);
        Rectangle rect = new Rectangle(column * pixelModifier, row * pixelModifier, pixelModifier, pixelModifier);
        return rect.contains(new Point(x, y));
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public Room getRoom() {
        return room;
    }

    //public abstract void draw(Graphics g, Board b, int z, boolean highlight);
    public abstract void draw(Graphics g, Board b);


}
