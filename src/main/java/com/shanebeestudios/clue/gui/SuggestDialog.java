package com.shanebeestudios.clue.gui;

import com.shanebeestudios.clue.ClueGame;
import com.shanebeestudios.clue.board.cell.BoardCell;
import com.shanebeestudios.clue.game.card.Card;
import com.shanebeestudios.clue.game.Character;
import com.shanebeestudios.clue.game.Room;
import com.shanebeestudios.clue.game.card.Solution;
import com.shanebeestudios.clue.game.Weapon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SuggestDialog extends JDialog {

    public enum SuggestType {
        SUGGESTION, ACCUSATION
    }

    JButton submit, cancel;
    ClueGame game;
    JComboBox<String> rooms;
    JComboBox<String> characters;
    JComboBox<String> weapons;
    SuggestType t;

    public SuggestDialog(SuggestType t, ClueGame g) {
        super(g, "", true);
        rooms = new JComboBox<>();
        characters = new JComboBox<>();
        weapons = new JComboBox<>();
        game = g;
        this.t = t;
        createLayout(t, g);
        setSize(new Dimension(300, 200));
        setVisible(true);
    }

    public void createLayout(SuggestType t, ClueGame g) {
        setLayout(new GridLayout(4, 2));
        add(new JLabel("  Room"));
        if (t == SuggestType.ACCUSATION) {
            setTitle("Make an Accusation");
            for (Room room : Room.values()) {
                if (room != Room.CLOSET && room != Room.WALKWAY && room != Room.OUTSIDE) {
                    rooms.addItem(room.getName());
                }
            }
        } else {
            setTitle("Make a Guess");
            int row = g.getHumanPlayer().getRow();
            int col = g.getHumanPlayer().getColumn();
            BoardCell cell = g.getBoard().getRoomCellAt(row, col);
            rooms.addItem(cell.getRoom().getName());
        }
        add(rooms);
        add(new JLabel("  Person"));
        for (Character character : Character.values()) {
            characters.addItem(character.getName());
        }
        add(characters);
        add(new JLabel("  Weapon"));
        for (Weapon weapon : Weapon.values()) {
            weapons.addItem(weapon.getName());
        }
        add(weapons);
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

        @SuppressWarnings("ConstantConditions")
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == submit) {
                Card disprove = game.handleSuggestion(characters.getSelectedItem().toString(), rooms.getSelectedItem().toString(), weapons.getSelectedItem().toString(), game.getHumanPlayer());
                Solution s = new Solution(characters.getSelectedItem().toString(), weapons.getSelectedItem().toString(), rooms.getSelectedItem().toString());
                if (t == SuggestType.SUGGESTION) {
                    game.getControlPanel().getGuesstext().setText(characters.getSelectedItem().toString() + " " + rooms.getSelectedItem().toString() + " " + weapons.getSelectedItem().toString());
                    if (disprove != null) {
                        game.getControlPanel().getResponse().setText(disprove.getName());
                    } else {
                        game.getControlPanel().getResponse().setText("no response");
                    }
                }
                if (game.checkAccusation(s) && t == SuggestType.ACCUSATION) {
                    JOptionPane.showMessageDialog(game, "You are correct!\nGame will now end.", "You Win", JOptionPane.INFORMATION_MESSAGE, Icon.CLUE_LOGO);
                    System.exit(0);
                } else if (!game.checkAccusation(s) && t == SuggestType.ACCUSATION) {
                    JOptionPane.showMessageDialog(game, "That is not correct.\nGame will now end.", "Incorrect Guess", JOptionPane.INFORMATION_MESSAGE, Icon.CLUE_LOGO);
                    System.exit(0);
                }
            }
            setVisible(false);
            dispose();
        }

    }

}
