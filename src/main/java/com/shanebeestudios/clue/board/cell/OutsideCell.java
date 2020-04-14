package com.shanebeestudios.clue.board.cell;

import com.shanebeestudios.clue.board.Board;

import java.awt.*;

public class OutsideCell extends BoardCell {

    @Override
    public boolean isWalkway() {
        return false;
    }

    @Override
    public boolean isRoom() {
        return true;
    }

    @Override
    public boolean isDoorway() {
        return false;
    }

    @Override
    public boolean isOutside() {
        return true;
    }

    @Override
    public void draw(Graphics g, Board b) {
        int pixelModifier = Math.min(b.getSize().width / b.getNumColumns(), b.getSize().height / b.getNumRows());
        g.setColor(Color.PINK);
        g.fillRect(column * pixelModifier, row * pixelModifier, pixelModifier, pixelModifier);
    }

}
