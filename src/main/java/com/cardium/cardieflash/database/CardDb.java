package com.cardium.cardieflash.database;

import com.cardium.cardieflash.Card;
import com.cardium.cardieflash.database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class CardDb implements CardInterface {
    private Connection conn;

    public CardDb(Database database) {
        this.conn = database.getConnection();
    }

    public Card createNewCard(String front, String back) {
        String sql = "INSERT INTO CARDS(FRONT,BACK) VALUES(?,?)";
        String generatedColumns[] = { "ID" };
        try (PreparedStatement pstmt = conn.prepareStatement(sql, generatedColumns)) {
            pstmt.setString(1, front);
            pstmt.setString(2, back);
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();

            int id = rs.getInt(1);

            return new Card(id, front, back);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }
    }

    public Card createNewCard(Card card) {
        return createNewCard(card.getFront(), card.getBack());
    }

    public Boolean delete(int cid) {
        String sql = "DELETE FROM CARDS WHERE cid = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, cid);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public Card editCard(int cid, String side, String text) {
        switch (side.toUpperCase()) {
        case "FRONT":
            break;
        case "BACK":
            break;
        default:
            throw new IllegalArgumentException();
        }

        String sql = "UPDATE CARDS SET " + side.toUpperCase() + " = ? WHERE cid = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, text);
            pstmt.setInt(2, cid);
            pstmt.executeUpdate();
            return getSingle(cid);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }
    }

    public Card editCard(int cid, Card card) {
        editCard(cid, "FRONT", card.getFront());
        editCard(cid, "BACK", card.getBack());
        return getSingle(cid);

    }

    public ArrayList<Card> getAll() {
        String sql = "SELECT CID, FRONT, BACK FROM CARDS";
        ArrayList<Card> cardSet = new ArrayList<Card>();

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                cardSet.add(new Card(rs.getInt("CID"), rs.getString("FRONT"), rs.getString("BACK")));
            }
            return cardSet;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return cardSet;
        }

    }

    public Card getSingle(int cid) {
        String sql = "SELECT CID, FRONT, BACK FROM CARDS WHERE cid = ?";
        Card query;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, cid);
            ResultSet rs = pstmt.executeQuery();

            query = new Card(rs.getInt("CID"), rs.getString("FRONT"), rs.getString("BACK"));

            return query;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new Card("x", "x");
        }
    }
}