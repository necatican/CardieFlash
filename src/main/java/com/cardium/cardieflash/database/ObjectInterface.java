package com.cardium.cardieflash.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public interface ObjectInterface {
    public int insert(int id);
    public int delete(int id);
    public int update();
    public ResultSet query(String queury);

}