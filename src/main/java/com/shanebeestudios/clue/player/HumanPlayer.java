package com.shanebeestudios.clue.player;

import com.shanebeestudios.clue.board.Board;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class HumanPlayer extends Player {

    private boolean canMakeAccusation;

    public HumanPlayer(@NotNull String name, @NotNull Color color, int row, int column) {
        super(name, color, row, column);
        canMakeAccusation = true;
    }

    public HumanPlayer() {
        this.setHuman(true);
        canMakeAccusation = true;
    }

    public void setCanMakeAccusation(boolean canMakeAccusation) {
        this.canMakeAccusation = canMakeAccusation;
    }

    public void makeMove(@NotNull Board board) {
        board.highlightTargets(this.getRow(), this.getColumn());
    }

    public boolean isCanMakeAccusation() {
        return canMakeAccusation;
    }

}
