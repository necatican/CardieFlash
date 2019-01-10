package com.cardium.cardieflash.database;

import java.sql.ResultSet;
import java.util.ArrayList;

import com.cardium.cardieflash.Card;

public interface CardInterface {
    public int createNewCard(String front, String back);
    public int createNewCard(Card card);

    public int delete(int cid);

    public int update(int cid, Card card);
    public int update(int cid, Enum side, String text);

    public ArrayList<Card> getAll();
    public ArrayList<Card> getSingle(int cid);
    public ArrayList<Card> getAllWithTag(String tag);
    public ArrayList<Card> getAllWithTag(int tag);
    public ArrayList<Card> getDeck(String deck);
    public ArrayList<Card> getDeck(int deck);
}