package com.sifatsdroid.quizapp;


public class QuestionLoader {
    public Question question;

    public Question load() {
        question = new Question();
        question.load();
        if (question.isValid()) return question;
        else return null;
    }

}
