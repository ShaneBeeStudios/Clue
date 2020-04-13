package com.shanebeestudios.clue.misc;

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
		if(!this.person.equals(person))
			return false;
		if(!this.weapon.equals(weapon))
			return false;
		if(!this.room.equals(room))
			return false;
		return true;
	}
	
}
