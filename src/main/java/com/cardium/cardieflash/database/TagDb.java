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
import java.util.HashMap;

public class TagDb implements TagInterface {
    private Connection conn;

    public TagDb(Database database) {
        this.conn = database.getConnection();
    }

    public Tag createNewTag(String name) {
        String sql = "INSERT INTO TAGS(NAME) VALUES(?)";
        String generatedColumns[] = { "ID" };
        try (PreparedStatement pstmt = conn.prepareStatement(sql, generatedColumns)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            int id = rs.getInt(1);
            return new Tag(id, name);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }
    }

    public Boolean deleteTag(int tagId) {
        String sql = "DELETE FROM TAGS WHERE TAGID = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, tagId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public Tag updateTag(Tag tag) {

        String sql = "UPDATE TAGS SET NAME = ? WHERE TAGID = ?";

        Tag backup = getTag(tag.getTagId());
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tag.getName());
            pstmt.setInt(2, tag.getTagId());
            pstmt.executeUpdate();

            Tag temp = getTag(tag.getTagId());

            if (!temp.equals(tag)) {
                return updateTag(backup);
            }

            return temp;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }
    }

    public Tag getTag(int tagId) {
        String sql = "SELECT TAGID, NAME FROM TAGS WHERE TAGID = ?";
        Tag query;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, tagId);
            ResultSet rs = pstmt.executeQuery();

            query = new Tag(rs.getInt("TAGID"), rs.getString("NAME"));

            return query;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }
    }

    public Tag getTag(String tagName) {
        String sql = "SELECT TAGID, NAME FROM TAGS WHERE NAME = ?";
        Tag query;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, tagName);
            ResultSet rs = pstmt.executeQuery();

            query = new Tag(rs.getInt("TAGID"), rs.getString("NAME"));

            return query;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }
    }
    public ArrayList<Tag> getAllTags() {
        String sql = "SELECT TAGID, NAME FROM TAGS";
        ArrayList<Tag> tagSet = new ArrayList<Tag>();
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tagSet.add(new Tag(rs.getInt("TAGID"), rs.getString("NAME")));
            }
            return tagSet;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }
    }

    public boolean addCard(int cid, int tagId) {
        String sql = "INSERT INTO HASTAGS(CID,TAGID) VALUES(?,?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, tagId);
            pstmt.setInt(2, cid);
            pstmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }
    }

    public boolean addCard(Tag tag, Card card) {
        return addCard(tag.getTagId(), card.getCid());
    }

    public HashMap<Integer, Card> getCardsWithTag(ArrayList<Integer> tagId) {
        String questionMarks = "";
        for (int i = 0; i < tagId.size() - 1; i++) {
            questionMarks = questionMarks + "?,";
        }

        String sql = "SELECT CARDS.CID, CARDS.FRONT, CARDS.BACK FROM CARDS JOIN HASTAGS ON CARDS.CID = HASTAGS.CID WHERE HASTAGS.TAGID IN ("
                + questionMarks + " ?);";
        HashMap<Integer, Card> query = new HashMap<Integer, Card>();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < tagId.size(); i++) {
                pstmt.setInt(i + 1, tagId.get(i));
            }

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

    public HashMap<Integer, Card> getCardsWithTag(Tag... tags) {
        ArrayList<Integer> tagList = new ArrayList<Integer>();
        for (Tag tag : tags) {
            tagList.add(tag.getTagId());
        }
        return getCardsWithTag(tagList);
    }

    public HashMap<Integer, Card> getCardsWithTag(int... tags) {
        ArrayList<Integer> tagList = new ArrayList<Integer>();
        for (int tag : tags) {
            tagList.add(tag);
        }
        return getCardsWithTag(tagList);
    }

}
