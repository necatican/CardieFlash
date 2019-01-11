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

    public int getCid() {
        return this.cid;
    }

    public String getFront() {
        return this.front;
    }

    public String getBack() {
        return this.back;
    }

    public boolean checkAnswer(String answer) {
        return answer.toLowerCase().equals(this.back.toLowerCase());
    }

    public boolean equals(Card card) {
        return (card.getCid() == this.cid && card.getBack().equals(this.back) && card.getFront().equals(this.front));
    }
}
