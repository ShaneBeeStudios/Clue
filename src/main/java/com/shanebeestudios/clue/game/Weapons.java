package com.shanebeestudios.clue.game;

@SuppressWarnings("unused")
public enum Weapons {

    REVOLVER("Revolver"),
    CANDLESTICK("Candlestick"),
    LEAD_PIPE("Lead Pipe"),
    KNIFE("Knife"),
    ROPE("Rope"),
    WRENCH("Wrench");

    private final String name;

    Weapons(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
