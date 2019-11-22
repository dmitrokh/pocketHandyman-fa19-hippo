package com.example.pockethandyman;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Question {
    private String question;
    private String category;
    private String author;
    private List<Answer> answers;
    private List<String> tags;

    public Question(String question, String category) {
        this(question, category, new ArrayList<Answer>());
    }

    public Question(String question, String category, List<Answer> answers) {
        this.question = question;
        this.category = category;
        this.answers = answers;
        tags = new ArrayList<>();
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

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
