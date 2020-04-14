package com.shanebeestudios.clue.game;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public enum  Characters {

    MISS_SCARLETT("Miss Scarlet", Color.RED, 1, 17),
    PROFESSOR_PLUM("Professor Plum", Color.MAGENTA, 6, 1),
    COLONEL_MUSTARD("Colonel Mustard", Color.ORANGE, 8, 24),
    MR_GREEN("Mr. Green", Color.GREEN, 25, 10),
    MRS_WHITE("Mrs. White", Color.WHITE, 25, 15),
    MRS_PEACOCK("Mrs. Peacock", Color.BLUE,19, 1);

    private static final List<String> NAMES = new ArrayList<>();
    private final String name;
    private final Color color;
    private final int xPos;
    private final int yPos;

    Characters(String name, Color color, int xPos, int yPos) {
        this.name = name;
        this.color = color;
        this.xPos = xPos;
        this.yPos = yPos;
    }

    static {
        for (Characters character : values()) {
            NAMES.add(character.name);
        }
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

    public static List<String> getNames() {
        return NAMES;
    }

}
