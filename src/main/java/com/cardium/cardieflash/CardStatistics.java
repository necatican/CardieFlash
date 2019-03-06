package com.cardium.cardieflash;

public class CardStatistics {

    private int cardId;
    private int totalAskCount;
    private int correctAnswerCount;
    private double answerTimeAverage;
    private double accuracy;
    private String lastAsked;

    public CardStatistics(int cardId, int totalAskCount, int correctAnswerCount, double answerTimeAverage,
            String lastAsked) {
        this.cardId = cardId;
        this.totalAskCount = totalAskCount;
        this.answerTimeAverage = answerTimeAverage;
        this.correctAnswerCount = correctAnswerCount;
        this.accuracy = correctAnswerCount / totalAskCount;
        this.lastAsked = lastAsked;
    }

}
