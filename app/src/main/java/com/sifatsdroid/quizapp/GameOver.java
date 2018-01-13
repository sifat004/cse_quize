package com.sifatsdroid.quizapp;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
public class GameOver extends ActionBarActivity {
    public int score;
    public String mail;
    public String pass;
    public String mail_address;
    public SharedPreferences sharedPreferences;
    public static final String mypreference = "admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = this.getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        mail_address = sharedPreferences.getString("mail", "sif.sifat24@gmail.com");
        Intent intent = getIntent();
        score = intent.getIntExtra("Score", -1);
        if (score == -1) throw new RuntimeException("Score couldn't be loaded");
        setContentView(R.layout.activity_game_over);
        showFinalScore();
        sendMail();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game_over, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    //shows gameOver information
    private void showFinalScore() {
        TextView scoreText = (TextView) findViewById(R.id.score_text);
        scoreText.setText("Score: " + score);
    }

    //Another button was clicked
    public void buttonClick(View view) {
        switch (view.getId()) {
            case R.id.restart_button:
                Intent intent = new Intent(this, PlayGame.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.return_button:
                // ((LightQuiz) this.getApplicationContext()).clearQuestions();
                this.finish();
                break;
            default:
                throw new RuntimeException("Unknown button ID");
        }
    }

    public void sendMail() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Send Email to Teacher");
            builder.setMessage("Give your mail address");
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            builder.setView(input);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mail = input.getText().toString();
                    AlertDialog.Builder builder = new AlertDialog.Builder(GameOver.this);
                    builder.setTitle("Send Email to Teacher");
                    builder.setMessage("Please give your password");
                    final EditText input = new EditText(GameOver.this);
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    builder.setView(input);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            pass = input.getText().toString();
                            try {
                                BackgroundMail.newBuilder(GameOver.this)
                                        .withUsername(mail)
                                        .withPassword(pass)
                                        .withMailto("toufikrahman098@gmail.com")
                                        .withType(BackgroundMail.TYPE_PLAIN)
                                        .withSubject("Quiz Score")
                                        .withBody("The score of this student is: " + score)
                                        .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                                            @Override
                                            public void onSuccess() {
                                                Toast.makeText(GameOver.this, "Your result has been sent to the teacher", Toast.LENGTH_LONG);
                                            }
                                        })
                                        .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                                            @Override
                                            public void onFail() {
                                                Toast.makeText(GameOver.this, "Your result couldn't be sent....Please turn internet on...", Toast.LENGTH_LONG);
                                            }
                                        })
                                        .send();
                            } catch (Exception e) {
                                Toast.makeText(GameOver.this, "Mail couldn't be sent", Toast.LENGTH_LONG);
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        } catch (Exception e) {
            Toast.makeText(this, "mail is not sent", Toast.LENGTH_LONG);
        }
    }
}

