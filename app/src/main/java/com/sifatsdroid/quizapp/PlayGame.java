package com.sifatsdroid.quizapp;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
public class PlayGame extends ActionBarActivity {
    private final int questionsDelay = 100;
    private final int questionsPoints = 1;
    private int quizTime = 60;
    private int points;
    private int correctAnswer;
    private ImageView correctImg;
    private ImageView wrongImg;
    private TextView questionText;
    private TextView pointsText;
    private TextView countdownText;
    private Button[] answerButtons = new Button[4];
    private Button next;
    private CountDownTimer countdown;
    private QuestionsGenerator generator;
    private ProgressDialog progress;
    private String difficultySelection = null;
    private boolean gameFinished = false;
    SharedPreferences sharedPreferences;
    public static final String mypreference = "admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);
        sharedPreferences = this.getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        quizTime = sharedPreferences.getInt("duration", 60);
        Intent intent = getIntent();
        difficultySelection = intent.getStringExtra("Genre");
        next = (Button) findViewById(R.id.nxt);
        progress = new ProgressDialog(this);
        progress.setMessage("Loading Database ");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();
        new Thread(new Runnable() {
            public void run() {
                try {
                    loadQuestions();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                loadLayout();
                runOnUiThread(new Runnable() {
                    public void run() {
                        startGame();
                    }
                });
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_play_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                gameOver();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //countdown.cancel();
        //hideQuestionMultimedia();
        this.finish();
        gameFinished = true;
        // gameOver();
    }

    @Override
    public void onBackPressed() {
        // this.finish();
        gameOver();
    }

    //an answer was clicked
    public void answerClicked(View view) {
        int answer; //-1 by default
        buttonsActive(false);
        switch (view.getId()) {
            case R.id.answer_1:
                answer = 1;
                break;
            case R.id.answer_2:
                answer = 2;
                break;
            case R.id.answer_3:
                answer = 3;
                break;
            case R.id.answer_4:
                answer = 4;
                break;
            case R.id.nxt:
                answer = 5;
                break;
            default:
                throw new RuntimeException("Unknown button ID");
        }
        if (correctAnswer == answer) {
            correctAnswer();
            generator.removeQuestion(this);
            updateTexts();
            nextQuestion();
        } else if (answer == 5) nextQuestion();
        else {
            wrongAnswer();
            generator.removeQuestion(this);
            updateTexts();
            nextQuestion();
        }
    }

    private void setNextQuestion() {
        if (!gameFinished) {
            buttonsActive(true);
            Question quest = generator.getNextQuestion(this);//get a randomized question
            if (!quest.isValid()) throw new RuntimeException("Invalid Question");
            this.correctAnswer = quest.getCorrectAnswer();
            for (int i = 0; i < 4; i++)
                answerButtons[i].setText(quest.getAnswer(i)); //set questions layout
            hideAnswerImage();
            questionText.setText(quest.getText());
        }
    }

    private void nextQuestion() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (generator.size() == 0) gameOver(); //no more questions left
                else setNextQuestion();
            }
        }, questionsDelay);
    }

    //What happens when a correct answer was clicked
    private void correctAnswer() {
        points += questionsPoints;
        wrongImg.setVisibility(View.INVISIBLE);
        correctImg.setVisibility(View.VISIBLE);
        ((QuizApp) this.getApplicationContext()).soundHandler.playCorrectSound();
    }

    //What happens when a wrong answer was clicked
    private void wrongAnswer() {
        correctImg.setVisibility(View.INVISIBLE);
        wrongImg.setVisibility(View.VISIBLE);
        ((QuizApp) this.getApplicationContext()).soundHandler.playWrongSound();
    }

    //Hides any answer image (tick or cross)
    private void hideAnswerImage() {
        correctImg.setVisibility(View.INVISIBLE);
        wrongImg.setVisibility(View.INVISIBLE);
    }

    private void buttonsActive(boolean b) {
        for (Button button : answerButtons) {
            button.setClickable(b);
        }
    }

    private void loadLayout() {
        correctImg = (ImageView) findViewById(R.id.correct_img);
        wrongImg = (ImageView) findViewById(R.id.wrong_img);
        pointsText = (TextView) findViewById(R.id.points_text);
        countdownText = (TextView) findViewById(R.id.countdown_text);
        questionText = (TextView) findViewById(R.id.question);
        answerButtons[0] = (Button) findViewById(R.id.answer_1);
        answerButtons[1] = (Button) findViewById(R.id.answer_2);
        answerButtons[2] = (Button) findViewById(R.id.answer_3);
        answerButtons[3] = (Button) findViewById(R.id.answer_4);
        hideAnswerImage();
    }

    //Updates score texts
    private void updateTexts() {
        pointsText.setText("Score:" + points);
        countdownText.setText("");
    }

    private void gameOver() {
        SharedPreferences sharedPreferences;
        final String mypreference = "mypref";
        int n;
        sharedPreferences = this.getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        n = -1;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("index", n);
        editor.commit();
        gameFinished = true;
        countdown.cancel();
        buttonsActive(false);
        Intent intent = new Intent(this, GameOver.class);
        intent.putExtra("Score", points);
        startActivity(intent);
        this.finish();
    }

    //Starts game
    private void startGame() {
        SharedPreferences sharedPreferences;
        final String mypreference = "mypref";
        int n;
        sharedPreferences = this.getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        n = -1;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("index", n);
        editor.commit();
        points = 0;
        countdown = new CountDownTimer(quizTime * 1000, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdownText.setText(Integer.toString((int) (millisUntilFinished / 1000) + 1));
            }

            @Override
            public void onFinish() {
                gameOver();
            }
        }.start();
        gameFinished = false;
        buttonsActive(true);
        updateTexts();
        setNextQuestion();
        progress.dismiss();
        Toast toast = Toast.makeText(this, "Quiz is Ready", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void loadQuestions() throws IOException {
        if (!Question.isQuestionListReady()) {
            ((QuizApp) this.getApplicationContext()).loadRawQuestions(difficultySelection);
        }
        this.generator = new QuestionsGenerator();
    }
}