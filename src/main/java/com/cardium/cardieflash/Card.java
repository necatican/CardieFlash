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
        return cid;
    }

    public String getFront() {
        return front;
    }

    public String getBack() {
        return back;
    }

    public boolean checkAnswer(String userAnswer) {
        String answer = userAnswer.toLowerCase();
        String backLower = back.toLowerCase();

        if (answer.equals(backLower)) {
            return true;
        } else if (Math.abs(answer.length() - backLower.length()) < LEVENSHTEIN_TOLERANCE){
            if (Levenshtein.levenshteinDistance(answer, backLower) < LEVENSHTEIN_TOLERANCE) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean equals(Card card) {
        return (card.getCid() == cid && card.getBack().equals(back) && card.getFront().equals(front));
    }
}
