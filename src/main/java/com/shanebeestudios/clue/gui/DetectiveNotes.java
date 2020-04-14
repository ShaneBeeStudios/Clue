package com.shanebeestudios.clue.gui;

import com.shanebeestudios.clue.game.Characters;
import com.shanebeestudios.clue.game.Rooms;
import com.shanebeestudios.clue.game.Weapons;

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
        for (Characters character : Characters.values()) {
            people.add(new JCheckBox(character.getName()));
        }
        add(people);
    }

    public void createRooms() {
        JPanel rooms = new JPanel();
        rooms.setLayout(new GridLayout(5, 2));
        rooms.setBorder(new TitledBorder(new EtchedBorder(), "Rooms"));
        for (Rooms room : Rooms.values()) {
            if (room != Rooms.WALKWAY && room != Rooms.OUTSIDE && room != Rooms.CLOSET) {
                rooms.add(new JCheckBox(room.getName()));
            }
        }
        add(rooms);
    }

    public void createWeapons() {
        JPanel weapons = new JPanel();
        weapons.setLayout(new GridLayout(3, 3));
        weapons.setBorder(new TitledBorder(new EtchedBorder(), "Weapons"));
        for (Weapons weapon : Weapons.values()) {
            weapons.add(new JCheckBox(weapon.getName()));
        }
        add(weapons);
    }

    public void createPersonGuess() {
        JPanel personGuess = new JPanel();
        personGuess.setLayout(new BorderLayout());
        JComboBox people = new JComboBox();
        people.addItem("");
        for (Characters character : Characters.values()) {
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
        for (Rooms room : Rooms.values()) {
            if (room != Rooms.WALKWAY && room != Rooms.OUTSIDE && room != Rooms.CLOSET) {
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
        for (Weapons weapon : Weapons.values()) {
            weapons.addItem(weapon.getName());
        }
        weaponGuess.add(weapons, BorderLayout.CENTER);
        weaponGuess.setBorder(new TitledBorder(new EtchedBorder(), "Weapon Guess"));
        add(weaponGuess);
    }

}
