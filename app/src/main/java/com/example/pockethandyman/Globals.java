package com.example.pockethandyman;

import android.app.Application;
import java.util.HashMap;
import com.google.firebase.database.DataSnapshot;
import java.util.ArrayList;

public class Globals extends Application {
    private String curUser;
    private HashMap<Integer, Question> allQuestions = new HashMap<>();

    public HashMap<Integer, Question> getAllQuestions() {
        return allQuestions;
    }

    public void setAllQuestions(HashMap<Integer, Question> allQuestions) {
        this.allQuestions = allQuestions;
    }

    public String getCurUser() {
        return curUser;
    }

    public void setCurUser(String curUser) {
        this.curUser = curUser;
    }
}