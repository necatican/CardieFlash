package com.cardium.cardieflash.database;

import com.cardium.cardieflash.Deck;
import com.cardium.cardieflash.database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DeckDb {
    private Connection conn;

    public DeckDb(Database database) {
        this.conn = database.getConnection();
    }

    public Deck createNewDeck(String name) {
        String sql = "INSERT INTO DECKS(NAME) VALUES(?)";
        String generatedColumns[] = { "ID" };
        try (PreparedStatement pstmt = conn.prepareStatement(sql, generatedColumns)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();

            int id = rs.getInt(1);

            return new Deck(id, name);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }
    }

    public Boolean delete(int deckId) {
        String sql = "DELETE FROM DECKS WHERE deckId = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, deckId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public Deck updateDeck(Deck deck) {

        String sql = "UPDATE DECKS SET NAME = ? WHERE deckId = ?";

        Deck backup = getDeck(deck.getDeckId());
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, deck.getName());
            pstmt.setInt(2, deck.getDeckId());
            pstmt.executeUpdate();

            Deck temp = getDeck(deck.getDeckId());

            if (!temp.equals(deck)) {
                return updateDeck(backup);
            }

            return temp;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }
    }

    public Deck getDeck(int deckId) {
        String sql = "SELECT deckId, NAME FROM DECKS WHERE deckId = ?";
        Deck query;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, deckId);
            ResultSet rs = pstmt.executeQuery();

            query = new Deck(rs.getInt("deckId"), rs.getString("Name"));

            return query;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }
    }

}