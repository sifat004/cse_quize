package com.sifatsdroid.quizapp;
import android.content.Context;
import android.media.MediaPlayer;

import java.io.IOException;
public class SoundHandler {
    public Context context;
    private MediaPlayer correctAnswer;
    private MediaPlayer wrongAnswer;
    private MediaPlayer questionSound = null;

    public SoundHandler(Context context) {
        this.context = context;
        correctAnswer = MediaPlayer.create(context, R.raw.correct_answ);
        wrongAnswer = MediaPlayer.create(context, R.raw.wrong_answ);
        //   correctAnswer=MediaPlayer.create(context, R.raw.wrong_answ);
    }

    public void playCorrectSound() {
        correctAnswer.start();
    }

    public void playWrongSound() {
        wrongAnswer.start();
    }
}
