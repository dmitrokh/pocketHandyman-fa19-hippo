package com.example.pockethandyman;

import android.net.Uri;

import java.io.Serializable;

public class Answer implements Serializable {

    public String answerText;
    public String author;
    public Uri videoUri;

    // other potential fields: comments, upvotes?

    public Answer() {

    }

    public Answer(String answerText, String author, Uri videoUri) {
        this.answerText = answerText;
        this.author = author;
        this.videoUri = videoUri;
    }
}
