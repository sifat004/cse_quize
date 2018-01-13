package com.sifatsdroid.quizapp;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;


public class QuizApp extends Application {
    private final String databaseName = "lq.db";
    private final String databaseQuery = "SELECT * FROM QUESTIONS";
    public SoundHandler soundHandler;
    public SQLiteHelper database;

    @Override
    public void onCreate() {
        super.onCreate();

        soundHandler = new SoundHandler(this.getApplicationContext());
        database = new SQLiteHelper(this, databaseName);
    }

    public void loadRawQuestions() throws IOException {

        if (!database.openDataBase()) throw new RuntimeException("database not loaded");
        else {
            Cursor cursor = database.query(databaseQuery);
            Question.questionList = new QuestionSet(cursor);
        }
        database.close();
    }

    public void loadRawQuestions(String difficulty) throws IOException {
        SQLiteHelper database = new SQLiteHelper(this, databaseName);
        if (!database.openDataBase()) throw new RuntimeException("database not loaded");
        else {
            String query = databaseQuery;
            if (difficulty != null)
                query = query + " WHERE CATEGORY=\"" + difficulty.toUpperCase() + "\"";
            Cursor cursor = database.query(query);
            Question.questionList = new QuestionSet(cursor);
        }
        database.close();
    }

    public void clearQuestions() {
        if (Question.questionList != null) Question.questionList.clear();
        Question.questionList = null;
    }

}
