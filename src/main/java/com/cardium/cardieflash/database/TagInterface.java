package com.cardium.cardieflash.database;

import java.util.ArrayList;
import java.util.HashMap;

import com.cardium.cardieflash.Card;
import com.cardium.cardieflash.Tag;

public interface TagInterface {
    public Tag createNewTag(String name);

    public Boolean deleteTag(int tagId);

    public Tag updateTag(Tag tag);

    public Tag getTag(int tagId);

    public boolean addCard(int cid, int tagId);

    public boolean addCard(Tag tag, Card card);

    public HashMap<Integer, Card> getCardsWithTag(ArrayList<Integer> tagId);
}
