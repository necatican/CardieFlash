package com.cardium.cardieflash.tools;

public final class Levenshtein {

    public static int levenshteinDistance(String first, String second) {
        int lenFirst = first.length() + 1;
        int lenSecond = second.length() + 1;
        int[][] distance = createLevenshteinMatrix(lenFirst, lenSecond);

        for (int i = 1; i < lenFirst; i++) {
            char currentFirst = first.charAt(i - 1);
            for (int j = 1; j < lenSecond; j++) {
                char currentSecond = second.charAt(j - 1);

                // charEditValue is 0 if both chars are equal and 1 if they are different.
                int charEditValue;

                if (currentFirst == currentSecond) {
                    charEditValue = 0;
                } else {
                    charEditValue = 1;
                }

                distance[i][j] = Math.min(Math.min(distance[i - 1][j] + 1, distance[i][j - 1] + 1),
                        distance[i - 1][j - 1] + charEditValue);
            }
        }
        return distance[lenFirst - 1][lenSecond - 1];
    }

    public static int[][] createLevenshteinMatrix(int lenFirst, int lenSecond) {
        int[][] matrix = new int[lenFirst][lenSecond];
        for (int i = 0; i < lenFirst; i++) {
            matrix[i][0] = i;
        }
        for (int i = 0; i < lenSecond; i++) {
            matrix[0][i] = i;
        }
        return matrix;
    }
}