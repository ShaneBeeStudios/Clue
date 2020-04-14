package com.shanebeestudios.clue.game;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public enum Room {

    // Rooms
    BALLROOM("Ballroom", 'B', Color.LIGHT_GRAY),
    BILLIARD_ROOM("Billiard Room", 'R', Color.LIGHT_GRAY),
    CONSERVATORY("Conservatory", 'C', Color.LIGHT_GRAY),
    DINING_ROOM("Dining Room", 'D', Color.LIGHT_GRAY),
    HALL("Hall", 'H', Color.LIGHT_GRAY),
    KITCHEN("Kitchen", 'K', Color.LIGHT_GRAY),
    LIBRARY("Library", 'L', Color.LIGHT_GRAY),
    LOUNGE("Lounge", 'O', Color.LIGHT_GRAY),
    STUDY("Study", 'S', Color.LIGHT_GRAY),

    // Misc areas
    CLOSET("Closet", 'X', Color.DARK_GRAY),
    WALKWAY("Walkway", 'W', Color.YELLOW),
    OUTSIDE("Outside", '_', Color.PINK);

    private static final Map<java.lang.Character, Room> ROOMS_BY_KEY = new HashMap<>();
    private final String name;
    private final char key;
    private final Color color;

    Room(String name, char key, Color color) {
        this.name = name;
        this.key = key;
        this.color = color;
    }

    static {
        for (Room room : values()) {
            ROOMS_BY_KEY.put((room.getKey()), room);
        }
    }

    public static Room getByKey(char key) {
        return ROOMS_BY_KEY.get(key);
    }

    public String getName() {
        return name;
    }

    public char getKey() {
        return key;
    }

    public Color getColor() {
        return color;
    }
}
