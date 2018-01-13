package com.sifatsdroid.quizapp;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
/**
 * Created by demiurgosoft - 3/25/15
 */
public class Question {
    protected static QuestionSet questionList = null;
    private ArrayList<String> answers = new ArrayList<>();
    private int correctAnswer = -1; //from 1 to 4
    private String text = null;

    public static void setQuestionList(Cursor cursor) {
        questionList = new QuestionSet(cursor);
    }

    public static int getQuestionSize() {
        return questionList.size();
    }

    public static boolean isQuestionListReady() {
        return questionList != null;
    }

    public void load() {
        RawQuestion raw = questionList.getRawQuestion();
        if (raw != null) {
            loadText(raw);
            loadAnswers(raw);
        }
    }

    public boolean isValid() {
        if (text == null || text.length() == 0) return false;
        else if (answers.size() != 4) return false;
        else if (correctAnswer < 1 || correctAnswer > 4) return false;
        else {
            for (int i = 0; i < 4; i++) {
                if (answers.get(i).length() == 0) return false;
            }
        }
        return true;
    }

    public void randomizeAns() {
        String s = correctAnswerText();
        Collections.shuffle(answers);
        correctAnswer = (answers.indexOf(s)) + 1;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public String getAnswer(int index) {
        if (index > answers.size()) return null;
        else return answers.get(index);
    }

    public String getText() {
        return text;
    }

    protected void loadText(RawQuestion question) {
        text = question.text;
    }

    protected void loadAnswers(RawQuestion question) {
        correctAnswer = 1;
        answers.clear();
        answers.add(question.correctAnswer);
        answers.add(question.answer1);
        answers.add(question.answer2);
        answers.add(question.answer3);
    }

    private String correctAnswerText() {
        return answers.get(correctAnswer - 1);
    }
}
