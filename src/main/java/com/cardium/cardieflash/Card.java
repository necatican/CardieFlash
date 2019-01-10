package com.cardium.cardieflash;

public class Card {
    private final int cid;
    private final String front;
    private final String back;

    public Card(int id, String question, String answer) {
        this.cid = id;
        this.front = question;
        this.back = answer;
    }

    public Card(String question, String answer) {
        this.cid = -1;
        this.front = question;
        this.back = answer;
    }

    public int getCid() {
        return this.cid;
    }

    public String getFront() {
        return this.front;
    }

    public String getBack() {
        return this.back;
    }

    public Boolean checkAnswer(String answer) {
        return answer.toLowerCase().equals(this.back.toLowerCase());
    }
}
