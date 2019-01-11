package com.cardium.cardieflash;

import java.util.HashMap;

public class Deck {
    private int deckId;
    private String name;
    private HashMap<Integer, Card> cards;

    public Deck(int id, String name, HashMap<Integer, Card> cards) {
        this.deckId = id;
        this.name = name;
        this.cards = cards;
    }

    public Deck(int id, String name) {
        this.deckId = id;
        this.name = name;
    }

    public int getDeckId() {
        return deckId;
    }

    public String getName() {
        return name;
    }

    public HashMap<Integer, Card> getCards() {
        return cards;
    }

    public boolean addCard(Card card) {
        if (cards.containsKey(card.getCid())) {
            return false;
        } else {
            cards.put(card.getCid(), card);
            return true;

        }
    }

    public boolean removeCard(int cid) {
        if (!cards.containsKey(cid)) {
            return false;
        } else {
            cards.remove(cid);
            return true;
        }
    }

    public boolean removeCard(Card card) {
        return removeCard(card.getCid());
    }

    public boolean editCard(Card card) {
        if (!cards.containsKey(card.getCid())) {
            return false;
        } else {
            cards.put(card.getCid(), card);
            return true;
        }

    }

}