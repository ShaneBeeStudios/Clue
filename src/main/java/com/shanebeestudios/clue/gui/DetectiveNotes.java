package com.shanebeestudios.clue.gui;

import com.shanebeestudios.clue.game.Character;
import com.shanebeestudios.clue.game.Room;
import com.shanebeestudios.clue.game.Weapon;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

@SuppressWarnings({"unchecked", "rawtypes"})
public class DetectiveNotes extends JDialog {

    public DetectiveNotes() {
        setSize(600, 450);
        setTitle("Detective Notes");
        setLayout(new GridLayout(3, 2));
        createLayout();
    }

    public void createLayout() {
        createPeople();
        createPersonGuess();
        createRooms();
        createRoomGuess();
        createWeapons();
        createWeaponGuess();
    }

    public void createPeople() {
        JPanel people = new JPanel();
        people.setLayout(new GridLayout(3, 3));
        people.setBorder(new TitledBorder(new EtchedBorder(), "People"));
        for (Character character : Character.values()) {
            people.add(new JCheckBox(character.getName()));
        }
        add(people);
    }

    public void createRooms() {
        JPanel rooms = new JPanel();
        rooms.setLayout(new GridLayout(5, 2));
        rooms.setBorder(new TitledBorder(new EtchedBorder(), "Rooms"));
        for (Room room : Room.values()) {
            if (room != Room.WALKWAY && room != Room.OUTSIDE && room != Room.CLOSET) {
                rooms.add(new JCheckBox(room.getName()));
            }
        }
        add(rooms);
    }

    public void createWeapons() {
        JPanel weapons = new JPanel();
        weapons.setLayout(new GridLayout(3, 3));
        weapons.setBorder(new TitledBorder(new EtchedBorder(), "Weapons"));
        for (Weapon weapon : Weapon.values()) {
            weapons.add(new JCheckBox(weapon.getName()));
        }
        add(weapons);
    }

    public void createPersonGuess() {
        JPanel personGuess = new JPanel();
        personGuess.setLayout(new BorderLayout());
        JComboBox people = new JComboBox();
        people.addItem("");
        for (Character character : Character.values()) {
            people.addItem(character.getName());
        }
        personGuess.add(people, BorderLayout.CENTER);
        personGuess.setBorder(new TitledBorder(new EtchedBorder(), "Person Guess"));
        add(personGuess);
    }

    public void createRoomGuess() {
        JPanel roomGuess = new JPanel();
        roomGuess.setLayout(new BorderLayout());
        JComboBox rooms = new JComboBox();
        rooms.addItem("");
        for (Room room : Room.values()) {
            if (room != Room.WALKWAY && room != Room.OUTSIDE && room != Room.CLOSET) {
                rooms.addItem(room.getName());
            }
        }
        roomGuess.add(rooms, BorderLayout.CENTER);
        roomGuess.setBorder(new TitledBorder(new EtchedBorder(), "Room Guess"));
        add(roomGuess);
    }

    public void createWeaponGuess() {
        JPanel weaponGuess = new JPanel();
        weaponGuess.setLayout(new BorderLayout());
        JComboBox weapons = new JComboBox();
        weapons.addItem("");
        for (Weapon weapon : Weapon.values()) {
            weapons.addItem(weapon.getName());
        }
        weaponGuess.add(weapons, BorderLayout.CENTER);
        weaponGuess.setBorder(new TitledBorder(new EtchedBorder(), "Weapon Guess"));
        add(weaponGuess);
    }

}
