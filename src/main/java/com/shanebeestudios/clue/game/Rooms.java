package com.shanebeestudios.clue.game;

@SuppressWarnings("unused")
public enum Rooms {

    BALLROOM("Ballroom", 'B'),
    BILLIARD_ROOM("Billiard Room", 'R'),
    CLOSET("Closet", 'X'),
    CONSERVATORY("Conservatory", 'C'),
    DINING_ROOM("Dining Room", 'D'),
    HALL("Hall", 'H'),
    KITCHEN("Kitchen", 'K'),
    LIBRARY("Library", 'L'),
    LOUNGE("Lounge", 'O'),
    STUDY("Study", 'S'),
    WALKWAY("Walkway", 'W');

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
