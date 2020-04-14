package com.shanebeestudios.clue.game;

@SuppressWarnings("unused")
public enum Weapon {

    REVOLVER("Revolver"),
    CANDLESTICK("Candlestick"),
    LEAD_PIPE("Lead Pipe"),
    KNIFE("Knife"),
    ROPE("Rope"),
    WRENCH("Wrench");

    private final String name;

    Weapon(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
