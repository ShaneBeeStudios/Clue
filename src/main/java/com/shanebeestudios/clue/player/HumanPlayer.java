package com.shanebeestudios.clue.player;

import com.shanebeestudios.clue.board.Board;

public class HumanPlayer extends Player {

    private boolean canMakeAccusation;

    public HumanPlayer() {
        this.setHuman(true);
        canMakeAccusation = true;
    }

    public void setCanMakeAccusation(boolean canMakeAccusation) {
        this.canMakeAccusation = canMakeAccusation;
    }

    public HumanPlayer(String name, String color, int row, int column) {
        super(name, color, row, column);
        canMakeAccusation = true;
    }

    public void makeMove(Board board) {
        board.highlightTargets(this.getRow(), this.getColumn());
    }

    public boolean isCanMakeAccusation() {
        return canMakeAccusation;
    }

}
