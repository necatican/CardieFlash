package com.cardium.cardieflash.database;

import java.util.ArrayList;

import com.cardium.cardieflash.Card;

public interface CardInterface {
    public int createNewCard(String front, String back);
    public int createNewCard(Card card);

    public int delete(int cid);

    public int update(int cid, Card card);
    public int update(int cid, String side, String text);

    public ArrayList<Card> getAll();
    public Card getSingle(int cid);
}