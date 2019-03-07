package com.cardium.cardieflash;

import java.util.HashMap;
import lombok.Getter;
import lombok.Setter;

public class Deck {
    @Getter
    private int deckId;

    @Getter
    @Setter
    private String name;

    @Getter
    private HashMap<Integer, Card> cards;

    @Getter
    @Setter
    private int totalCardCount;

    @Getter
    @Setter
    private String color;

    public Deck(int id, String name, HashMap<Integer, Card> cards) {
        this.deckId = id;
        this.name = name;
        this.cards = cards;
    }

    public Deck(int id, String name) {
        this.deckId = id;
        this.name = name;
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