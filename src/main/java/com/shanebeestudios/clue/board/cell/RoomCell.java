/*
 * Class: RoomCell, child of BoardCell
 * Authors: Brandon Rodriguez, Hunter Lang
 * This class is used to keep track of the room cells in the game.
 * Also encapsulates the doorDirection attribute
 */
package com.shanebeestudios.clue.board.cell;

import com.shanebeestudios.clue.board.Board;
import com.shanebeestudios.clue.game.Room;

import java.awt.*;

@SuppressWarnings({"unused", "deprecation"})
public class RoomCell extends BoardCell {

    private boolean isDoor = false; // Start with this cell not being a door
    private final DoorDirection doorDirection; // Instantiate a blank door direction
    private final char roomClassifier; // Create a blank classifier
    private boolean drawName = false;

    public RoomCell(Room room, char roomClassifier) {
        super(room);
        this.roomClassifier = roomClassifier;

        //-------------------------------------
        highlight = false;
        //-------------------------------

        // Set the direction based on the second char
        // U = UP, D = DOWN, L = LEFT, R = RIGHT
        switch (roomClassifier) {
            case 'U':
                doorDirection = DoorDirection.UP;
                isDoor = true;
                break;
            case 'D':
                doorDirection = DoorDirection.DOWN;
                isDoor = true;
                break;
            case 'R':
                doorDirection = DoorDirection.RIGHT;
                isDoor = true;
                break;
            case 'L':
                doorDirection = DoorDirection.LEFT;
                isDoor = true;
                break;
            case 'N':
                drawName = true;
            default:
                doorDirection = DoorDirection.NONE;
        }
    }

    @Override
    public boolean isRoom() {
        return true;
    }

    @Override
    public boolean isDoorway() {
        return isDoor;
    }

    public DoorDirection getDoorDirection() {
        return doorDirection;
    }

//    public char getRoomClassifier() {
//        return roomClassifier;
//    }

    @Override
    public void draw(Graphics g, Board b) {
        int doorFraction = 5;
        //int pixelModifier = 25;
        int pixelModifier = Math.min(b.size().width / b.getNumColumns(), b.size().height / b.getNumRows());
        int doorOffset = pixelModifier / doorFraction;
        b.setPixelModifier(pixelModifier);

        g.setColor(room.getColor());
        if (this.highlight) {
            g.setColor(Color.GREEN);
        }

        g.fillRect(column * pixelModifier, row * pixelModifier, pixelModifier, pixelModifier);
        g.setColor(Color.blue);
        if (this.isDoorway() && (doorDirection.equals(DoorDirection.UP))) {
            g.fillRect(column * pixelModifier, row * pixelModifier, pixelModifier, doorOffset);
        } else if (this.isDoorway() && (doorDirection.equals(DoorDirection.DOWN))) {
            g.fillRect(column * pixelModifier, (row * pixelModifier + pixelModifier) - doorOffset, pixelModifier, doorOffset);
        } else if (this.isDoorway() && (doorDirection.equals(DoorDirection.LEFT))) {
            g.fillRect(column * pixelModifier, row * pixelModifier, doorOffset, pixelModifier);
        } else if (this.isDoorway() && (doorDirection.equals(DoorDirection.RIGHT))) {
            g.fillRect((column * pixelModifier + pixelModifier) - doorOffset, row * pixelModifier, doorOffset, pixelModifier);
        }
        if (drawName) {
            char[] chars = room.getName().toCharArray();
            g.drawChars(chars, 0, chars.length, column * pixelModifier, row * pixelModifier);
        }
    }

}
