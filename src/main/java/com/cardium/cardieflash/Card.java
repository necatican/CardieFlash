package com.cardium.cardieflash;

import com.cardium.cardieflash.tools.Levenshtein;

public class Card {
    private final int cid;
    private final String front;
    private final String back;
    private static final int LEVENSHTEIN_TOLERANCE = 3;

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

    public boolean checkAnswer(String userAnswer) {
        String answer = userAnswer.toLowerCase();
        String back = this.back.toLowerCase();

        boolean exact = answer.equals(back);
        boolean close = Math.abs(answer.length() - back.length()) < LEVENSHTEIN_TOLERANCE;

        return exact || (close && Levenshtein.levenshteinDistance(answer, back) < LEVENSHTEIN_TOLERANCE);
    }

    public boolean equals(Card card) {
        return (card.getCid() == this.cid && card.getBack().equals(this.back) && card.getFront().equals(this.front));
    }
}
