package com.sifatsdroid.quizapp;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Collections;
/**
 * Created by demiurgosoft - 5/4/15
 * Stored data from database ordered
 */
public class QuestionSet {
    private ArrayList<RawQuestion> questions = new ArrayList<>();
    private int index;

    //load from Cursor (database)
    public QuestionSet(Cursor cursor) {
        if (cursor.moveToFirst()) {
            do {
                RawQuestion question = new RawQuestion(cursor);
                questions.add(question);
            } while (cursor.moveToNext());
        }
        randomize_questionList();
    }

    public void clear() {
        questions.clear();
        index = 0;
    }

    public int size() {
        return questions.size();
    }

    public void randomize_questionList() {
        index = 0;
        Collections.shuffle(questions);
    }

    public RawQuestion getRawQuestion() {
        if (index >= questions.size()) return null;
        else {
            index++;
            return questions.get(index - 1);
        }
    }
}
