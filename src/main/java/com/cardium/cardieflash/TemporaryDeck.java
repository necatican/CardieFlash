package com.cardium.cardieflash;

import java.util.HashMap;

public class TemporaryDeck {
    private String name;
    private HashMap<Integer, Card> cards;

    public TemporaryDeck(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addCards(HashMap<Integer, Card> cards) {
        this.cards = cards;
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

