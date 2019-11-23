package com.example.pockethandyman;

import android.app.Application;
import java.util.HashMap;

public class Globals extends Application {
    private HashMap<Integer, Question> allQuestions = new HashMap<>();

    public HashMap<Integer, Question> getAllQuestions() {
        return allQuestions;
    }

    public void setAllQuestions(HashMap<Integer, Question> allQuestions) {
        this.allQuestions = allQuestions;
    }
}