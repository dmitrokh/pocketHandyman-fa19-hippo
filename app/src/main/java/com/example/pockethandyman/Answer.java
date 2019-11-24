package com.example.pockethandyman;

import java.io.Serializable;

public class Answer implements Serializable {

    public String answerText;
    public String author;

    public Answer() {

    }

    public Answer(String answerText, String author) {
        this.answerText = answerText;
        this.author = author;
    }
}
