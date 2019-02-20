package com.cardium.cardieflash;

import com.cardium.cardieflash.database.CardDb;
import com.cardium.cardieflash.database.Database;
import com.cardium.cardieflash.database.DeckDb;
import com.cardium.cardieflash.database.TagDb;

public class Main {
    public static void main(String args[]) {
        Database database = new Database("jdbc:sqlite:src/main/resources/db/cards.db");
        database.startConnection();
        CardDb cardDb = new CardDb(database);
        DeckDb deckDb = new DeckDb(database);
        TagDb tagDb = new TagDb(database);
    }
}
