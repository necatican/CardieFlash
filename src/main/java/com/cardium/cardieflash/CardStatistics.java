package com.cardium.cardieflash;

import java.util.ArrayList;

public class CardStatistics {
    private static final int MAX_DATA_ENTRIES = 50;

    private int cardId;
    private int totalAskCount;
    private int correctAnswerCount;
    private double answerTimeAverage;
    private double accuracy;
    private String lastAsked;

    private ArrayList<AnswerData> cardAnswers;

    public CardStatistics(int cardId, int totalAskCount, int correctAnswerCount, double answerTimeAverage,
            String lastAsked) {
        this.cardId = cardId;
        this.totalAskCount = totalAskCount;
        this.answerTimeAverage = answerTimeAverage;
        this.correctAnswerCount = correctAnswerCount;
        this.accuracy = correctAnswerCount / totalAskCount;
        this.lastAsked = lastAsked;
    }

    public void setCardAnswers(ArrayList<AnswerData> cardAnswers)
    {
        this.cardAnswers = cardAnswers;
    }
}
