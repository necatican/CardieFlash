package com.cardium.cardieflash.database;

import com.cardium.cardieflash.AnswerDataList;
import com.cardium.cardieflash.AnswerData;
import com.cardium.cardieflash.Card;
import com.cardium.cardieflash.database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AnswerDb {
    private Connection conn;
    private static final int MAX_DATA_ENTRIES = 50;

    public AnswerDb(Database database) {
        this.conn = database.getConnection();
    }

    public void submitAnswer(double timeToAnswer, boolean correctness, int cardId) {
        String sql = "INSERT INTO ANSWERDATA(TIMETOANSWER, CORRECTNESS, LASTASKED) VALUES(?, ?, DATETIME('NOW'));";
        String generatedColumns[] = { "ID" };
        try (PreparedStatement pstmt = this.conn.prepareStatement(sql, generatedColumns)) {
            pstmt.setDouble(1, timeToAnswer);
            pstmt.setBoolean(2, correctness);
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();

            int answerId = rs.getInt(1);
            submitAnswerRelation(answerId, cardId);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }
    }

    public void submitAnswerRelation(int answerId, int cardId) {
        String sql = "INSERT INTO HASANSWERDATA(ANSWERID, CID) VALUES(?, ?)";
        try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
            pstmt.setInt(1, answerId);
            pstmt.setInt(2, cardId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }
    }

    public AnswerDataList getAnswersForCard(int cardId) {
        /** This method returns last MAX_DATA_ENTRIES answers for given cardId. */

        String sql = "SELECT ANSWERDATA.ANSWERID, ANSWERDATA.TIMETOANSWER, ANSWERDATA.CORRECTNESS, ANSWERDATA.LASTASKED FROM ANSWERDATA JOIN "
                + "HASANSWERDATA ON ANSWERDATA.ANSWERID = HASANSWERDATA.ANSWERID WHERE CID = 1 ORDER BY ANSWERDATA.ANSWERID DESC LIMIT ?;";
        ArrayList<AnswerData> query = new ArrayList<AnswerData>();
        try (PreparedStatement pstmt = this.conn.prepareStatement(sql)) {

            pstmt.setInt(1, cardId);
            pstmt.setInt(2, MAX_DATA_ENTRIES);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                query.add(new AnswerData(rs.getInt("ANSWERID"), rs.getDouble("TIMETOANSWER"),
                        rs.getBoolean("CORRECTNESS"), rs.getString("LASTASKED")));
            }
            return new AnswerDataList(query, cardId);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }
    }

    public AnswerDataList getAnswersForCard(Card card) {
        return getAnswersForCard(card.getCid());
    }
}
