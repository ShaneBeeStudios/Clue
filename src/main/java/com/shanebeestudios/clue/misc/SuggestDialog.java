package com.shanebeestudios.clue.misc;

import com.shanebeestudios.clue.ClueGame;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;


public class SuggestDialog extends JDialog {

    public enum SuggestType {
        SUGGESTION, ACCUSATION;
    }

    JButton submit, cancel;
    ClueGame game;
    JComboBox<String> room;
    JComboBox<String> person;
    JComboBox<String> weapon;
    SuggestType t;

    public SuggestDialog(SuggestType t, ClueGame g) {
        super(g, "", true);
        room = new JComboBox<>();
        person = new JComboBox<>();
        weapon = new JComboBox<>();
        game = g;
        this.t = t;
        createLayout(t, g);
        setSize(new Dimension(300, 200));
        setVisible(true);
    }

    public void createLayout(SuggestType t, ClueGame g) {
        setLayout(new GridLayout(4, 2));
        add(new JLabel("Room"));
        if (t == SuggestType.ACCUSATION) {
            setTitle("Make an Accusation");
            room.addItem("Conservatory");
            room.addItem("Kitchen");
            room.addItem("Ballroom");
            room.addItem("Billiard Room");
            room.addItem("Library");
            room.addItem("Study");
            room.addItem("Dining Room");
            room.addItem("Lounge");
            room.addItem("Walkway");
            room.addItem("Closet");
            room.addItem("Hall");
            add(room);
        } else {
            setTitle("Make a Guess");
            room.addItem(g.getBoard().getRooms().get(g.getBoard().getRoomCellAt(g.getHumanPlayer().getRow(), g.getHumanPlayer().getColumn()).getRoomClassifier()));
        }
        add(room);
        add(new JLabel("Person"));
        person.addItem("Miss Scarlet");
        person.addItem("Mr. Green");
        person.addItem("Mrs. White");
        person.addItem("Colonel Mustard");
        person.addItem("Professor Plum");
        person.addItem("Mrs. Peacock");
        add(person);
        add(new JLabel("Weapon"));
        weapon.addItem("Revolver");
        weapon.addItem("Knife");
        weapon.addItem("Rope");
        weapon.addItem("Lead Pipe");
        weapon.addItem("Candlestick");
        weapon.addItem("Wrench");
        add(weapon);
        add(submit());
        add(cancel());
    }

    public JButton submit() {
        submit = new JButton("Submit");
        submit.addActionListener(new ButtonListener());
        return submit;
    }

    public JButton cancel() {
        cancel = new JButton("Cancel");
        cancel.addActionListener(new ButtonListener());
        return cancel;
    }

    class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == submit) {
                Card disprove = game.handleSuggestion(person.getSelectedItem().toString(), room.getSelectedItem().toString(), weapon.getSelectedItem().toString(), game.getHumanPlayer());
                Solution s = new Solution(person.getSelectedItem().toString(), weapon.getSelectedItem().toString(), room.getSelectedItem().toString());
                if (t == SuggestType.SUGGESTION) {
                    game.getControlPanel().getGuesstext().setText(person.getSelectedItem().toString() + " " + room.getSelectedItem().toString() + " " + weapon.getSelectedItem().toString());
                    if (disprove != null) {
                        game.getControlPanel().getResponse().setText(disprove.getName());
                    } else {
                        game.getControlPanel().getResponse().setText("no response");
                    }
                }
                if (game.checkAccusation(s) && t == SuggestType.ACCUSATION) {
                    JOptionPane.showMessageDialog(game, "You are correct! Game will now end.", "You Win", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                } else if (!game.checkAccusation(s) && t == SuggestType.ACCUSATION) {
                    JOptionPane.showMessageDialog(game, "That is not correct.", "Incorrect Guess", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            setVisible(false);
            dispose();
        }

    }

}
