package com.cardium.cardieflash.database;

import java.sql.*;

public class Database {
   private String url;
   private Connection conn;

   public Database(String url) {
      this.url = url;
   }

   public void startConnection() {
      try {
         Class.forName("org.sqlite.JDBC");
         conn = DriverManager.getConnection(url);
         conn.setAutoCommit(true);
      } catch (Exception e) {
         System.err.println(e.getClass().getName() + ": " + e.getMessage());
         System.exit(0);
      }
   }

   public Connection getConnection() {
      return conn;
   }
}
