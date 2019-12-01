package com.example.pockethandyman;

import java.io.Serializable;

public class Answer implements Serializable {

    public String answerText;
    public String author;
    public String videoFileName;

    // other potential fields: comments, upvotes?

    public Answer() {

    }

    public Answer(String answerText, String author, String videoFileName) {
        this.answerText = answerText;
        this.author = author;
        this.videoFileName = videoFileName;
    }
}
