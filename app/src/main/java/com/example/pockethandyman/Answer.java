package com.example.pockethandyman;

import android.net.Uri;

import java.io.Serializable;

public class Answer implements Serializable {

    public String answerText;
    public String author;
    public String videoUriString;

    // other potential fields: comments, upvotes?

    public Answer() {

    }

    public Answer(String answerText, String author, String videoUriString) {
        this.answerText = answerText;
        this.author = author;
        this.videoUriString = videoUriString;
    }
}
