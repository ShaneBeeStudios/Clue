package com.shanebeestudios.clue.game.card;

import java.util.ArrayList;
import java.util.List;

/**
 * A deck of cards
 */
@SuppressWarnings("unused")
public class Deck extends ArrayList<Card> {

    /**
     * Get cards from the deck by type
     *
     * @param type CardType to check
     * @return All cards which match type
     */
    public List<Card> getCards(CardType type) {
        List<Card> cards = new ArrayList<>();
        for (Card card : this) {
            if (card.getCardType() == type) {
                cards.add(card);
            }
        }
        return cards;
    }

}
