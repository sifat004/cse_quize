package com.sifatsdroid.quizapp;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
public class Add_ques extends Activity {
    EditText q, ca, a1, a2, a3, cat;
    Button save;
    String ques, coorect_ans, ans_1, ans_2, ans_3, category;
    private final String databaseName = "lq.db";
    private final String databaseQuery = "SELECT * FROM QUESTIONS";
    public SoundHandler soundHandler;
    SQLiteHelper database;
    String TABLE_NAME = "QUESTIONS";
    String COLUMN_CA = "CA";
    String COLUMN_A1 = "A1";
    String COLUMN_A2 = "A2";
    String COLUMN_A3 = "A3";
    String COLUMN_CAT = "CATEGORY";
    String COLUMN_SOUND = "SOUND_NAME";
    String COLUMN_IMAGE = "IMAGE_NAME";
    String COLUMN_QUES = "QUESTION";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_ques);
        q = (EditText) findViewById(R.id.question);
        ca = (EditText) findViewById(R.id.correct_ans);
        a1 = (EditText) findViewById(R.id.ans_1);
        a2 = (EditText) findViewById(R.id.ans_2);
        a3 = (EditText) findViewById(R.id.ans_3);
        cat = (EditText) findViewById(R.id.category);
        save = (Button) findViewById(R.id.save_to_DB);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ques = q.getText().toString();
                coorect_ans = ca.getText().toString();
                ans_1 = a1.getText().toString();
                ans_2 = a2.getText().toString();
                ans_3 = a3.getText().toString();
                category = cat.getText().toString();
                addQuestion(ques, coorect_ans, ans_1, ans_2, ans_3, category, null, null);
                Toast.makeText(Add_ques.this, "Question is added to DB", Toast.LENGTH_LONG);
                Intent intent = new Intent(Add_ques.this, Admin.class);
                startActivity(intent);
            }
        });
    }

    public void addQuestion(String question, String coorect_ans, String ans_1, String ans_2, String ans_3, String category, String sound, String image) {
        database = new SQLiteHelper(this, databaseName);
        SQLiteDatabase db = database.getWritableDatabase();
        String sql = "INSERT INTO " + TABLE_NAME + " (" + COLUMN_QUES +
                ", " + COLUMN_CA + ", " + COLUMN_A1 +
                ", " + COLUMN_A2 + ", " + COLUMN_A3 +
                ", " + COLUMN_CAT + ", " + COLUMN_SOUND + "," + COLUMN_IMAGE + ") VALUES ('" + question +
                "', '" + coorect_ans + "', '" + ans_1 + "','" + ans_2 + "', '" + ans_3 + "', '" + category + "', '" + sound + "', '" + image + "') ";
        db.execSQL(sql);
    }
}
