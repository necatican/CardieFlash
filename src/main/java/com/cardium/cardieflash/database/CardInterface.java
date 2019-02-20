package com.cardium.cardieflash.database;

import java.util.ArrayList;

import com.cardium.cardieflash.Card;

public interface CardInterface {
    public Card createNewCard(String front, String back);

    public Card createNewCard(Card card);

    public Boolean delete(int cid);

    public Card updateCard(Card card);

    public ArrayList<Card> getAll();

    public Card getSingle(int cid);
}
