package com.shanebeestudios.clue.board.cell;

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
