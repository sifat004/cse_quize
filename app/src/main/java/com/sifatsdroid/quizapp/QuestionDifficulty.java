package com.sifatsdroid.quizapp;


public enum QuestionDifficulty {
    EASY, MEDIUM, HARD;


    public static String[] names() {
        QuestionDifficulty[] questions = QuestionDifficulty.values();
        String[] result = new String[questions.length];

        for (int i = 0; i < questions.length; i++) {
            result[i] = questions[i].toString();
        }
        return result;
    }

}
