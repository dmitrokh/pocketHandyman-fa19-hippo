package com.example.pockethandyman;

import android.app.Application;

import java.util.ArrayList;

public class Globals extends Application {
    ArrayList<Question> allQuestions = new ArrayList<>();

    public ArrayList<Question> getAllQuestions() {
        return allQuestions;
    }

    public void setAllQuestions(ArrayList<Question> allQuestions) {
        this.allQuestions = allQuestions;
    }
}