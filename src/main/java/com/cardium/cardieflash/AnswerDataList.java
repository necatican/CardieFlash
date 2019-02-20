package com.cardium.cardieflash;

import java.util.ArrayList;

public class AnswerDataList {
    private ArrayList<AnswerData> answerDataList;
    private int cardId;

    public AnswerDataList(ArrayList<AnswerData> answerDataList, int cardId) {
        this.answerDataList = answerDataList;
        this.cardId = cardId;
    }

    public ArrayList<AnswerData> getAnswerDataList() {
        return this.answerDataList;
    }

    public CardStatistics getCardStats() {
        int totalAskCount = this.answerDataList.size();
        double totalTime = 0;
        String lastAsked = this.answerDataList.get(0).getLastAsked();
        int totalCorrectAnswers = 0;

        for(AnswerData answerData : this.answerDataList) {
            if(answerData.getCorrectness()) {
                totalCorrectAnswers += 1;
            }   
            totalTime += answerData.getTimeToAnswer();
        }
        double timeAvg = totalTime / totalAskCount;

        return new CardStatistics(this.cardId, totalAskCount, totalCorrectAnswers, timeAvg, lastAsked);
    }
}
