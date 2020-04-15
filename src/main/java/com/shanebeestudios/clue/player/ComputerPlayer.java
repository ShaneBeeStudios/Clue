package com.shanebeestudios.clue.player;

import com.shanebeestudios.clue.ClueGame;
import com.shanebeestudios.clue.board.Board;
import com.shanebeestudios.clue.board.cell.BoardCell;
import com.shanebeestudios.clue.board.cell.RoomCell;
import com.shanebeestudios.clue.game.card.Card;
import com.shanebeestudios.clue.game.card.CardType;
import com.shanebeestudios.clue.game.card.Deck;
import com.shanebeestudios.clue.game.Room;
import com.shanebeestudios.clue.game.card.Solution;
import com.shanebeestudios.clue.game.Suggestion;
import com.shanebeestudios.clue.gui.Icon;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

@SuppressWarnings("unused")
public class ComputerPlayer extends Player {

    private Room lastRoomVisited;
    private Suggestion accusation;

    public ComputerPlayer(@NotNull String name, @NotNull Color color, int row, int column) {
        super(name, color, row, column);
        accusation = null;
    }

    public ComputerPlayer() {
        super();
    }

    public Suggestion createSuggestion(int row, int column, Deck deck, Board board) {
        Suggestion suggestion = new Suggestion();
        Room room = board.getRoomCellAt(getRow(), getColumn()).getRoom();
        suggestion.setRoom(new Card(room.getName(), CardType.ROOM));
        Collections.shuffle(deck);
        suggestion.setPerson(findValidCard(deck, CardType.PERSON));
        suggestion.setWeapon(findValidCard(deck, CardType.WEAPON));
        return suggestion;
    }

    public Card findValidCard(Deck deck, CardType type) {
        Deck knownCards = this.getKnownCards();
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
                if (room.getRoom() != lastRoomVisited) {
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
                lastRoomVisited = room.getRoom();

                // create a suggestion
                Suggestion s = createSuggestion(getRow(), getColumn(), game.getDeck(), board);
                // call disproveSuggestion
                Card disprove = game.handleSuggestion(s.getPerson().getName(), s.getRoom().getName(), s.getWeapon().getName(), this);


                // update control panel
                game.getControlPanel().getGuesstext().setText(s.getPerson().getName() + " " + s.getRoom().getName() + " " + s.getWeapon().getName());
                game.getControlPanel().getResponse().setText(" ");
                /* Why exactly would we be showing this to the human player?
                // Lets hold onto this just in case
                if (disprove != null) {
                    game.getControlPanel().getResponse().setText(disprove.getName());
                } else {
                    game.getControlPanel().getResponse().setText("no response");
                    accusation = s;
                }
                 */
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

    public void updateSeen(List<Card> seen) {
        knownCards.addAll(seen);
    }

    public Room getLastRoomVisited() {
        return lastRoomVisited;
    }

    public void setLastRoomVisited(Room lastRoomVisited) {
        this.lastRoomVisited = lastRoomVisited;
    }

}
