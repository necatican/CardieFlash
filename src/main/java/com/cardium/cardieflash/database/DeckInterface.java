package com.cardium.cardieflash.database;

import java.util.HashMap;

import com.cardium.cardieflash.Card;
import com.cardium.cardieflash.Deck;

public interface DeckInterface {
    public Deck createNewDeck(String name);

    public Boolean delete(int deckId);

    public Deck updateDeck(Deck deck);

    public Deck getDeck(int deckId);

    public boolean addCard(int deckId, int cid);

    public boolean addCard(Deck deck, Card card);

    public HashMap<Integer, Card> getCardsInDeck(Deck deck);
}
