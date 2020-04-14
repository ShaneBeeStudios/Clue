package com.shanebeestudios.clue.game;

@SuppressWarnings("unused")
public enum Rooms {

    // Rooms
    BALLROOM("Ballroom", 'B'),
    BILLIARD_ROOM("Billiard Room", 'R'),
    CONSERVATORY("Conservatory", 'C'),
    DINING_ROOM("Dining Room", 'D'),
    HALL("Hall", 'H'),
    KITCHEN("Kitchen", 'K'),
    LIBRARY("Library", 'L'),
    LOUNGE("Lounge", 'O'),
    STUDY("Study", 'S'),

    // Misc areas
    CLOSET("Closet", 'X'),
    WALKWAY("Walkway", 'W'),
    OUTSIDE("Outside", '_');

    private final String name;
    private final Character key;

    Rooms(String name, Character key) {
        this.name = name;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public Character getKey() {
        return key;
    }

}
