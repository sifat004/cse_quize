package com.sifatsdroid.quizapp;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
public class Admin extends AppCompatActivity {
    Button addQues;
    Button save_mail, save_duration;
    EditText mail;
    EditText duration;
    String mail_address;
    int quiz_duration;
    SharedPreferences sharedPreferences;
    public static final String mypreference = "admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        sharedPreferences = this.getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        addQues = (Button) findViewById(R.id.addQues);
        save_mail = (Button) findViewById(R.id.button);
        mail = (EditText) findViewById(R.id.editText);
        duration = (EditText) findViewById(R.id.editText_duration);
        save_duration = (Button) findViewById(R.id.button_time);
        mail.setText(getMail());
        duration.setText(String.valueOf(getQuizTime()));
        addQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin.this, Add_ques.class);
                startActivity(intent);
            }
        });
        save_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMail();
            }
        });
        save_duration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setQuizTime();
            }
        });
    }

    public void setMail() {
        mail_address = mail.getText().toString();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("mail", mail_address);
        editor.commit();
    }

    public String getMail() {
        mail_address = sharedPreferences.getString("mail", "toufikrahman098@gmail.com");
        return mail_address;
    }

    public void setQuizTime() {
        quiz_duration = Integer.parseInt(duration.getText().toString());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("duration", quiz_duration);
        editor.commit();
    }

    public int getQuizTime() {
        quiz_duration = sharedPreferences.getInt("duration", 60);
        return quiz_duration;
    }
}
