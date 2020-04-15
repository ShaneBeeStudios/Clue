package com.shanebeestudios.clue.game;

import com.shanebeestudios.clue.game.card.Card;

public class Suggestion {

    private Card person;
    private Card room;
    private Card weapon;

    public Card getPerson() {
        return person;
    }

    public void setPerson(Card person) {
        this.person = person;
    }

    public Card getRoom() {
        return room;
    }

    public void setRoom(Card room) {
        this.room = room;
    }

    public Card getWeapon() {
        return weapon;
    }

    public void setWeapon(Card weapon) {
        this.weapon = weapon;
    }

    public boolean checkSolution(String person, String weapon, String room) {
        if (!this.person.getName().equals(person))
            return false;
        if (!this.weapon.getName().equals(weapon))
            return false;
        return this.room.getName().equals(room);
    }

}
