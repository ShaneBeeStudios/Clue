package com.shanebeestudios.clue.player;

import com.shanebeestudios.clue.ClueGame;
import com.shanebeestudios.clue.board.Board;
import com.shanebeestudios.clue.board.cell.BoardCell;
import com.shanebeestudios.clue.board.cell.RoomCell;
import com.shanebeestudios.clue.game.Card;
import com.shanebeestudios.clue.game.CardType;
import com.shanebeestudios.clue.game.Icon;
import com.shanebeestudios.clue.game.Solution;
import com.shanebeestudios.clue.game.Suggestion;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ComputerPlayer extends Player {

    private char lastRoomVisited;
    private Suggestion accusation;

    public ComputerPlayer(String name, Color color, int row, int column) {
        super(name, color, row, column);
        accusation = null;
    }

    public ComputerPlayer() {
        super();
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    public Suggestion createSuggestion(int row, int column, List<Card> deck, Board board) {
        Suggestion suggestion = new Suggestion();
        String room = board.getRooms().get(board.getRoomCellAt(row, column).getRoom().getName());
        suggestion.setRoom(new Card(room, CardType.ROOM));
        Collections.shuffle(deck);
        suggestion.setPerson(findValidCard(deck, CardType.PERSON));
        suggestion.setWeapon(findValidCard(deck, CardType.WEAPON));
        return suggestion;
    }

    public Card findValidCard(List<Card> deck, CardType type) {
        List<Card> knownCards = this.getKnownCards();
        for (Card x : deck) {
            if (x.getCardType().equals(type) && !knownCards.contains(x)) {
                return x;
            }
        }
        return null;
    }

    public BoardCell pickLocation(Set<BoardCell> targets) {

        for (BoardCell selection : targets) {
            if (selection.isRoom()) {
                RoomCell room = (RoomCell) selection;
                if (room.getRoom().getKey() != lastRoomVisited) {
                    return selection;
                }
            }
        }
        Random generator = new Random();
        int random = generator.nextInt();
        random = Math.abs(random % targets.size());
        Object[] targArr = targets.toArray();
        return (BoardCell) targArr[random];
    }

    public void makeMove(Board board, int roll, ClueGame game) {
        if (!(accusation == null)) {
            makeAccusation(game);
        } else {
            board.startTargets(board.calcIndex(this.getRow(), this.getColumn()), roll);
            BoardCell choice = pickLocation(board.getTargets());
            for (int x = 0; x < board.getNumColumns(); x++) {
                for (int y = 0; y < board.getNumRows(); y++) {
                    if (board.getCellAt(y, x).equals(choice)) {
                        this.setColumn(x);
                        this.setRow(y);
                    }
                }
            }
            if (board.getCellAt(getRow(), getColumn()).isRoom()) {
                // set flag for last room
                RoomCell room = (RoomCell) board.getCellAt(getRow(), getColumn());
                lastRoomVisited = room.getRoom().getKey();

                // create a suggestion
                Suggestion s = createSuggestion(getRow(), getColumn(), game.getDeck(), board);
                // call disproveSuggestion
                Card disprove = game.handleSuggestion(s.getPerson().getName(), s.getRoom().getName(), s.getWeapon().getName(), this);


                // update control panel
                game.getControlPanel().getGuesstext().setText(s.getPerson().getName() + " " + s.getRoom().getName() + " " + s.getWeapon().getName());
                if (disprove != null) {
                    game.getControlPanel().getResponse().setText(disprove.getName());
                } else {
                    game.getControlPanel().getResponse().setText("no response");
                    accusation = s;
                }
            }
            board.repaint();
        }
    }

    public void makeAccusation(ClueGame game) {
        //display computers accusation and if it is correct.
        //System.out.println("accusation" + accusation.getPerson() + " " + accusation.getRoom() + " " + accusation.getWeapon());
        Solution solution = new Solution(accusation.getPerson().getName(), accusation.getWeapon().getName(), accusation.getRoom().getName());
        if (game.checkAccusation(solution)) {
            JOptionPane.showMessageDialog(game, "Computer wins! It was " + accusation.getPerson().getName() + " in the " + accusation.getRoom().getName() +
                    " with the " + accusation.getWeapon().getName() + ". The game will now exit.", "Game Over", JOptionPane.INFORMATION_MESSAGE, Icon.CLUE_LOGO);
        }
    }

    public void updateSeen(Card seen) {
        knownCards.add(seen);
    }

    public void updateSeen(ArrayList<Card> seen) {
        knownCards.addAll(seen);
    }

    public char getLastRoomVisited() {
        return lastRoomVisited;
    }

    public void setLastRoomVisited(char lastRoomVisited) {
        this.lastRoomVisited = lastRoomVisited;
    }

}
