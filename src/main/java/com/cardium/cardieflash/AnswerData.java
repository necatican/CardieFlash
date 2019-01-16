package com.cardium.cardieflash;

public class AnswerData {
    private int answerId;
    private double timeToAnswer;
    private boolean correctness;
    private String lastAsked;

    public AnswerData(int id, double timeToAnswer, boolean correctness, String lastAsked) {
        this.answerId = id;
        this.timeToAnswer = timeToAnswer;
        this.correctness = correctness;
        this.lastAsked = lastAsked;
    }

}
