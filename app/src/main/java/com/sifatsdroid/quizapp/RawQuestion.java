package com.sifatsdroid.quizapp;
import android.database.Cursor;
/**
 * Created by demiurgosoft - 5/4/15
 */
public class RawQuestion {
    public static final String textattr = "QUESTION";
    private static final String correctAnswerattr = "CA";
    private static final String answer2attr = "A1";
    private static final String answer3attr = "A2";
    private static final String answer4attr = "A3";
    private static final String soundattr = "SOUND_NAME";
    private static final String imageattr = "IMAGE_NAME";
    public String text;
    public String correctAnswer;
    public String answer1;
    public String answer2;
    public String answer3;
    public String sound;
    public String image;

    public RawQuestion(Cursor cursor) {
        correctAnswer = cursor.getString(cursor.getColumnIndex(correctAnswerattr));
        answer1 = cursor.getString(cursor.getColumnIndex(answer2attr));
        answer2 = cursor.getString(cursor.getColumnIndex(answer3attr));
        answer3 = cursor.getString(cursor.getColumnIndex(answer4attr));
        text = cursor.getString(cursor.getColumnIndex(textattr));
        sound = cursor.getString(cursor.getColumnIndex(soundattr));
        image = cursor.getString(cursor.getColumnIndex(imageattr));
    }
}

