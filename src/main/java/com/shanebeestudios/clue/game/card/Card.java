package com.shanebeestudios.clue.game.card;

@SuppressWarnings("unused")
public class Card {

    private String name;
    private CardType type;

    public Card(String name, CardType type) {
        super();
        this.name = name;
        this.type = type;
    }

    public CardType getCardType() {
        return type;
    }

    public void setCardType(CardType cardType) {
        this.type = cardType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Overriding the equals function
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    // Still overriding the equal function. spell check is for casuals
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Card other = (Card) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return type == other.type;
    }

}
