package com.example.pockethandyman;

import android.app.Application;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class Globals extends Application {
    ArrayList<Question> allQuestions = new ArrayList<>();

    public ArrayList<Question> getAllQuestions() {
        return allQuestions;
    }

    public void retrieveQuestionsFromDB(DataSnapshot allQuestionsSnapshot) {
        allQuestions = new ArrayList<>();
        for (DataSnapshot ds : allQuestionsSnapshot.getChildren()) {
            final String question = ds.child("question").getValue(String.class);
            final String category = ds.child("category").getValue(String.class);
            final String author = ds.child("author").getValue(String.class);

            // Get answer IDs within question
            DataSnapshot answersSnapshot = ds.child("answers");
            ArrayList<Answer> answers = new ArrayList<>();
            // Get each answer from the question
            for (DataSnapshot childSnapshot : answersSnapshot.getChildren()) {
                Answer answer = childSnapshot.getValue(Answer.class);
                answers.add(answer);
            }

            Question q = new Question(question, category, author, answers);
            allQuestions.add(q);
        }
    }
}