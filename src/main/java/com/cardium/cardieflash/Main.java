package com.cardium.cardieflash;

import java.sql.*;

public class Main {
  public static void main( String args[] ) {
     Card card = new Card(1, "Hello", "HI");
     System.out.println(card.checkAnswer("hi"));
   }
}