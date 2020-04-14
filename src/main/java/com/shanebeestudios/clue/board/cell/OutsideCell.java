package com.shanebeestudios.clue.board.cell;

import com.shanebeestudios.clue.board.Board;
import com.shanebeestudios.clue.game.Room;

import java.awt.*;

public class OutsideCell extends BoardCell {

    public OutsideCell(Room room) {
        super(room);
    }

    @Override
    public boolean isOutside() {
        return true;
    }

    @Override
    public void draw(Graphics g, Board b) {
        int pixelModifier = Math.min(b.getSize().width / b.getNumColumns(), b.getSize().height / b.getNumRows());
        g.setColor(room.getColor());
        g.fillRect(column * pixelModifier, row * pixelModifier, pixelModifier, pixelModifier);
    }

}
