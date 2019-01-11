package com.cardium.cardieflash.database;

import com.cardium.cardieflash.Deck;

public interface DeckInterface {
    public Deck createNewDeck(String name);

    public Boolean delete(int did);

    public Deck updateDeck(Deck deck);

    public Deck getDeck(int did);
}