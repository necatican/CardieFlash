package com.cardium.cardieflash.database;

import com.cardium.cardieflash.Card;
import com.cardium.cardieflash.Tag;
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

    public Boolean delete(Card card) {
        return delete(card.getCid());
    }

    public Card updateCard(Card card) {

        String sql = "UPDATE CARDS SET FRONT = ?, BACK = ? WHERE cid = ?";

        Card backup = getSingle(card.getCid());
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, card.getFront());
            pstmt.setString(2, card.getBack());
            pstmt.setInt(3, card.getCid());
            pstmt.executeUpdate();

            Card temp = getSingle(card.getCid());

            if (!temp.equals(card)) {
                return updateCard(backup);
            }

            return temp;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }
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
            throw new RuntimeException();
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
            throw new RuntimeException();
        }
    }

    public ArrayList<Tag> getTags(int cid) {
        String sql = "SELECT TAGS.TAGID, TAGS.NAME FROM TAGS JOIN HASTAGS ON TAGS.TAGID = HASTAGS.TAGID WHERE HASTAGS.CID = ?";
        ArrayList<Tag> query = new ArrayList<Tag>();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, cid);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                query.add(new Tag(rs.getInt("TAGID"), rs.getString("NAME")));
            }
            return query;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }
    }

    public ArrayList<Tag> getTags(Card card) {
        return getTags(card.getCid());
    }
}