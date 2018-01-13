package com.sifatsdroid.quizapp;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Collections;

public class QuestionsGenerator {
    Question q = new Question();
    private ArrayList<Question> questionsList;

    //Default constructor
    public QuestionsGenerator() {
        questionsList = new ArrayList<>();
        loadQuestions();
        shuffle();
    }

    public QuestionsGenerator(int number) {
        questionsList = new ArrayList<>();
        loadQuestions(number);
        shuffle();
    }

    //true if it have at least one valid question
    public boolean empty() {
        return questionsList.isEmpty();
    }

    public void shuffle() {
        Collections.shuffle(this.questionsList);
    }

    //return a question (runtime exception if empty), removes question from list
    public Question getNextQuestion(Context context) {
        if (questionsList.isEmpty()) throw new RuntimeException("QuestionGenerator empty");
        else {
            SharedPreferences sharedPreferences;
            final String mypreference = "mypref";
            int n;
            sharedPreferences = context.getSharedPreferences(mypreference,
                    Context.MODE_PRIVATE);
            n = sharedPreferences.getInt("index", -1);
            n++;
            if (n >= questionsList.size()) n = 0;
            Question res = questionsList.get(n);
            if (res == null) {
                n = n + 1;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("index", n);
                editor.commit();
                return getNextQuestion(context);
            }
            //questionsList.remove(0);
            else {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("index", n);
                editor.commit();
                if (!res.isValid())
                    throw new RuntimeException("Question not valid"); //read another question?
                res.randomizeAns();
                return res;
            }
        }
    }

    public Question getPrevQuestion(Context context) {
        if (questionsList.isEmpty()) throw new RuntimeException("QuestionGenerator empty");
        else {
            SharedPreferences sharedPreferences;
            final String mypreference = "mypref";
            int n;
            sharedPreferences = context.getSharedPreferences(mypreference,
                    Context.MODE_PRIVATE);
            n = sharedPreferences.getInt("index", -1);
            if (n > 0) n--;
            Question res = questionsList.get(n);
            //questionsList.remove(0);
            if (res == null) {
                n = n - 1;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("index", n);
                editor.commit();
                return getNextQuestion(context);
            } else {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("index", n);
                editor.commit();
                if (!res.isValid())
                    throw new RuntimeException("Question not valid"); //read another question?
                res.randomizeAns();
                return res;
            }
        }
    }

    public void removeQuestion(Context context) {
        if (questionsList.isEmpty()) throw new RuntimeException("QuestionGenerator empty");
        else {
            SharedPreferences sharedPreferences;
            final String mypreference = "mypref";
            int n;
            sharedPreferences = context.getSharedPreferences(mypreference,
                    Context.MODE_PRIVATE);
            n = sharedPreferences.getInt("index", 0);
            questionsList.remove(n);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("index", n);
            editor.commit();
        }
    }

    //adds a question if valid
    private boolean addQuestion(Question q) {
        boolean valid = q.isValid();
        if (valid) {
            questionsList.add(q);
        }
        return valid;
    }

    //return generator size
    public int size() {
        return questionsList.size();
    }

    private void loadQuestions(int questionNumber) {
        if (questionNumber > Question.getQuestionSize())
            questionNumber = Question.getQuestionSize();
        if (questionNumber <= 0) throw new RuntimeException("raw questions empty");
        QuestionLoader questionLoader = new QuestionLoader();
        for (int i = 0; i < questionNumber; i++) {
            Question quest = null;
            do {
                quest = questionLoader.load();
            } while (quest == null);
            addQuestion(quest);
        }
        Question.questionList.randomize_questionList();
    }

    private void loadQuestions() {
        this.loadQuestions(Question.getQuestionSize());
    }
}
