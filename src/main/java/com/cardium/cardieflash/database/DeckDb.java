package com.cardium.cardieflash.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import com.cardium.cardieflash.Card;
import com.cardium.cardieflash.Deck;

public class DeckDb implements DeckInterface {
    static final String DEFAULT_COLOR = "0xfffff0";

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

    public Boolean delete(Deck deck) {
        return this.delete(deck.getDeckId());
    }

    public Deck updateDeck(Deck deck) {

        String sql = "UPDATE DECKS SET NAME = ? WHERE deckId = ?";

        Deck backup = getDeck(deck.getDeckId());
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, deck.getName());
            pstmt.setInt(2, deck.getDeckId());
            pstmt.executeUpdate();

            Deck temp = getDeck(deck.getDeckId());

            if (!temp.getName().equals(deck.getName())) {
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

    public ArrayList<Deck> getAllDecks() {
        String sql = "SELECT deckId, NAME FROM DECKS";
        ArrayList<Deck> deckList = new ArrayList<Deck>();
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                deckList.add(new Deck(rs.getInt("deckId"), rs.getString("Name")));
            }

            return deckList;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }

    }

    public int getTotalCardCount(int deckId) {
        String sql = "SELECT COUNT(CID) AS cardCount FROM HASCARDS WHERE deckId = ?";
        int query;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, deckId);
            ResultSet rs = pstmt.executeQuery();

            query = rs.getInt("cardCount");

            return query;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }
    }

    public boolean addCard(int deckId, int cid) {
        String sql = "INSERT INTO HASCARDS(DECKID,CID) VALUES(?,?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, deckId);
            pstmt.setInt(2, cid);
            pstmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }
    }

    public boolean addCard(Deck deck, Card card) {
        return addCard(deck.getDeckId(), card.getCid());
    }

    public HashMap<Integer, Card> getCardsInDeck(int deckId) {
        String sql = "SELECT CARDS.CID, CARDS.FRONT, CARDS.BACK FROM CARDS JOIN HASCARDS ON CARDS.CID = HASCARDS.CID WHERE HASCARDS.DECKID = ?;";
        HashMap<Integer, Card> query = new HashMap<Integer, Card>();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, deckId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                query.put(rs.getInt("CID"), new Card(rs.getInt("CID"), rs.getString("FRONT"), rs.getString("BACK")));
            }
            return query;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }
    }

    public HashMap<Integer, Card> getCardsInDeck(Deck deck) {
        return getCardsInDeck(deck.getDeckId());
    }

    public boolean setColor(int deckId, String color) {
        String sql = "INSERT INTO HASCOLOR(DECKID,COLOR) VALUES(?,?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, deckId);
            pstmt.setString(2, color);
            pstmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }

    }

    public boolean setColor(Deck deck, String color) {
        return this.setColor(deck.getDeckId(), color);
    }

    public boolean updateColor(int deckId, String color) {
        String sql = "UPDATE HASCOLOR SET COLOR = ? WHERE DECKID = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, color);
            pstmt.setInt(2, deckId);
            pstmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }
    }

    public boolean updateColor(Deck deck, String color) {
        return this.updateColor(deck.getDeckId(), color);

    }

    public String getColor(int deckId) {
        String sql = "SELECT COLOR FROM HASCOLOR WHERE DECKID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, deckId);
            ResultSet rs = pstmt.executeQuery();
            return rs.getString("COLOR");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return DEFAULT_COLOR;
        }
    }

    public String getColor(Deck deck) {
        return this.getColor(deck.getDeckId());
    }

    public String getDefaultColor() {
        return DEFAULT_COLOR;
    }
}