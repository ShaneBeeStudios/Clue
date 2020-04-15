package com.shanebeestudios.clue.player;

import com.shanebeestudios.clue.board.Board;
import com.shanebeestudios.clue.game.card.Card;
import com.shanebeestudios.clue.game.card.CardType;
import com.shanebeestudios.clue.game.card.Deck;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

@SuppressWarnings("unused")
public class Player {

    private int row;
    private int column;
    private char room;
    private String name;
    protected Deck knownCards;
    private Color color;
    private Deck cards;
    private boolean highlight;
    private boolean human;

    public Player(@NotNull String name, @NotNull Color color, int row, int column) {
        this.row = row;
        this.column = column;
        this.name = name;
        this.color = color;
        cards = new Deck();
        knownCards = new Deck();
        human = false;
    }

    public Player() {
        cards = new Deck();
        knownCards = new Deck();
        human = false;
    }

    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public boolean isHuman() {
        return human;
    }

    public void setHuman(boolean human) {
        this.human = human;
    }

    public boolean isHighlight() {
        return highlight;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    public char getRoom() {
        return room;
    }

    public void setRoom(char room) {
        this.room = room;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public Deck getCards() {
        return cards;
    }

    public void setCards(@NotNull Deck cards) {
        this.cards = cards;
    }

    public void resetCards() {
        knownCards = new Deck();
        cards = new Deck();
    }

    public void giveCard(@NotNull Card card) {
        cards.add(card);
    }

    public Deck getKnownCards() {
        return knownCards;
    }

    public void setKnownCards(@NotNull Deck knownCards) {
        this.knownCards = knownCards;
    }

    public Card disproveSuggestion(String person, String room, String weapon) {
        if (cards.contains(new Card(person, CardType.PERSON)))
            return cards.get(cards.indexOf(new Card(person, CardType.PERSON)));
        if (cards.contains(new Card(room, CardType.ROOM)))
            return cards.get(cards.indexOf(new Card(room, CardType.ROOM)));
        if (cards.contains(new Card(weapon, CardType.WEAPON)))
            return cards.get(cards.indexOf(new Card(weapon, CardType.WEAPON)));
        return null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((color == null) ? 0 : color.hashCode());
        result = prime * result + column;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + row;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Player other = (Player) obj;
        if (color == null) {
            if (other.color != null)
                return false;
        } else if (!color.equals(other.color))
            return false;
        if (column != other.column)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return row == other.row;
    }

    public void draw(Graphics g, Board b) {
        int pixelModifier = Math.min(b.getSize().width / b.getNumColumns(), b.getSize().height / b.getNumRows());
        //int pixelModifier = 25;
        setColor(g);
        g.fillRoundRect(column * pixelModifier, row * pixelModifier, pixelModifier, pixelModifier, pixelModifier, pixelModifier);
    }

    public void setColor(Graphics g) {
        g.setColor(color);
    }

}



















