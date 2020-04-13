/*
 * Class: RoomCell, child of BoardCell
 * Authors: Brandon Rodriguez, Hunter Lang
 * This class is used to keep track of the room cells in the game.
 * Also encapsulates the doorDirection attribute
 */
package com.shanebeestudios.clue.board;

import java.awt.Color;
import java.awt.Graphics;

@SuppressWarnings({"unused", "deprecation"})
public class RoomCell extends BoardCell {

    private String roomName;

    public enum DoorDirection {

        // Declaration of the enum. Done such that the direction also tells which way the player should move on the com.shanebeestudios.clue.board
        NONE(0, 0), UP(-1, 0), DOWN(1, 0), LEFT(0, -1), RIGHT(0, 1);

        private final int x;
        private final int y;

        DoorDirection(int X, int Y) {
            x = X;
            y = Y;
        }

        // Getters for x and y
        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    private boolean isDoor = false; // Start with this cell not being a door
    private DoorDirection doorDirection; // Instantiate a blank door direction
    private final char roomClassifier; // Create a blank classifier
    private boolean drawName = false;

    public RoomCell(String roomName) {

        //-------------------------------------
        this.roomName = roomName;
        highlight = false;
        //-------------------------------

        roomClassifier = roomName.charAt(0);
        if (roomName.length() == 2 && roomName.charAt(1) != 'N') {
            isDoor = true; // This cell is a doorway

            // Set the direction based on the second char
            // U = UP, D = DOWN, L = LEFT, R = RIGHT
            if (roomName.charAt(1) == 'U') {
                doorDirection = DoorDirection.UP;
            } else if (roomName.charAt(1) == 'D') {
                doorDirection = DoorDirection.DOWN;
            } else if (roomName.charAt(1) == 'L') {
                doorDirection = DoorDirection.LEFT;
            } else if (roomName.charAt(1) == 'R') {
                doorDirection = DoorDirection.RIGHT;
            }
        } else {
            if (roomName.length() == 2 && roomName.charAt(1) == 'N') {
                drawName = true;
            }
            doorDirection = DoorDirection.NONE;
        }
    }

    //-------------------------------------------------
    public String getRoomName() {
        return roomName;
    }


    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
//----------------------------------------------------------

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

    public char getRoomClassifier() {
        return roomClassifier;
    }

    @Override
    //public void draw(Graphics g, Board b, int z, boolean highlight) {
    public void draw(Graphics g, Board b) {
        int doorFraction = 5;
        //int pixelModifier = 25;
        int pixelModifier = Math.min(b.size().width / b.getNumColumns(), b.size().height / b.getNumRows());
        int doorOffset = pixelModifier / doorFraction;
        b.setPixelModifier(pixelModifier);

        g.setColor(Color.LIGHT_GRAY);
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
            g.drawChars(b.getRooms().get(roomClassifier).toCharArray(), 0, b.getRooms().get(roomClassifier).length(), column * pixelModifier, row * pixelModifier);
        }
    }

}
