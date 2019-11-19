package com.example.pockethandyman;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Question {
    private String question;
    private String category;
    private List<Timestamp> answers;

    public Question(String question, String category) {
        this.question = question;
        this.category = category;
        answers = new ArrayList<>();
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Timestamp> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Timestamp> answers) {
        this.answers = answers;
    }
}
