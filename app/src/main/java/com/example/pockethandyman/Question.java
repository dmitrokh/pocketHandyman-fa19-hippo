package com.example.pockethandyman;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Question implements Serializable {
    private String question;
    private String category;
    private String author;
    private List<Answer> answers;
    private List<String> tags;

    public Question(String question, String category, String author) {
        this(question, category, author, new ArrayList<Answer>());
    }

    public Question(String question, String category, String author, List<Answer> answers) {
        this.question = question;
        this.category = category;
        this.author = author;
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

    public List<String> getTags() {
        return tags;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
