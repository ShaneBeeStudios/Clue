package com.shanebeestudios.clue.game.card;

public class Solution {

    private final String person;
    private final String weapon;
    private final String room;

    public Solution(String person, String weapon, String room) {
        super();
        this.person = person;
        this.weapon = weapon;
        this.room = room;
    }

    public boolean checkSolution(String person, String weapon, String room) {
        if (!this.person.equals(person))
            return false;
        if (!this.weapon.equals(weapon))
            return false;
        return this.room.equals(room);
    }

}
