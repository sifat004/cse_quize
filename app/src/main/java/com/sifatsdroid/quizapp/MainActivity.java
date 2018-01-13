package com.sifatsdroid.quizapp;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
public class MainActivity extends ActionBarActivity {
    public String[] difficulties;
    public AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.sifatsdroid.quizapp.R.layout.activity_main);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((QuizApp) this.getApplicationContext()).clearQuestions();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.sifatsdroid.quizapp.R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == com.sifatsdroid.quizapp.R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    public void startGame(View view) {
        switch (view.getId()) {
            case R.id.start_button:
                Intent intent = new Intent(this, PlayGame.class);
                startActivity(intent);
                break;
            case R.id.other_game_button:
                selectDifficulty();
                break;
            case R.id.admin:
                showDialog();
                break;
            default:
                throw new RuntimeException("Unknown button ID");
        }
    }

    public void selectDifficulty() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_title);
        difficulties = QuestionDifficulty.names();
        builder.setItems(QuestionDifficulty.names(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                beginQuizGame(QuestionDifficulty.names()[which]);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void beginQuizGame(String difficulty) {
        Intent intent = new Intent(this, PlayGame.class);
        intent.putExtra("Genre", difficulty);
        startActivity(intent);
    }

    public void showDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.password_admin, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);
        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setNegativeButton("Go",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        /** DO THE METHOD HERE WHEN PROCEED IS CLICKED*/
                        String user_text = (userInput.getText()).toString();
                        /** CHECK FOR USER'S INPUT **/
                        if (user_text.equals("cste")) {
                            Log.d(user_text, "HELLO THIS IS THE MESSAGE CAUGHT :)");
                            Intent intent2 = new Intent(MainActivity.this, Admin.class);
                            startActivity(intent2);
                        } else {
                            Log.d(user_text, "string is empty");
                            String message = "The password you have entered is incorrect." + " \n \n" + "Please try again!";
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("Error");
                            builder.setMessage(message);
                            builder.setPositiveButton("Cancel", null);
                            builder.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    showDialog();
                                }
                            });
                            builder.create().show();
                        }
                    }
                        })
                .setPositiveButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}


