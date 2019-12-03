package com.example.pockethandyman;

import java.io.Serializable;

public class Answer implements Serializable {

    public String answerText;
    public String questionText;
    public String author;
    public String videoFileName;
    public int numUpvotes;

    // other potential fields: comments, upvotes?

    public Answer() {

    }

    public Answer(String answerText, String questionText, String author, String videoFileName) {
        this.answerText = answerText;
        this.questionText = questionText;
        this.author = author;
        this.videoFileName = videoFileName;
    }

    public void addUpvote() {
        this.numUpvotes++;
    }

    public void removeUpvote() {
        this.numUpvotes--;
    }
}
