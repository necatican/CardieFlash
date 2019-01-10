package com.cardium.cardieflash.database;

import com.cardium.cardieflash.database.Database;   
import java.sql.Connection;

public class CardDb
{  
    private Database database;

    public CardDb(Database database)
    {
        this.database = database;
    }

    
    public int insert(String front, String back)
    {
        Connection conn = Database.conn;
        return 1;
    }
}
/* 
    public int insert(Cards card)

    public int delete(int cid);

    public int update(int cid, Cards card);
    public int update(int cid, Enum side, String text);

    public ResultSet getAll();
    public ResultSet getSingle(int cid);
    public ResultSet getAllWithTag(String tag);
    public ResultSet getAllWithTag(int tag);
    public ResultSet getAllInDeck(String deck);
    public ResultSet getAllInDeck(int deck);

*/