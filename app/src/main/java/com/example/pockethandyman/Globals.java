package com.example.pockethandyman;

import android.app.Application;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

            // Get answer IDs within question
            final DataSnapshot answersSnapshot = ds.child("answers");
            // Get table aof all answers
            final DatabaseReference answersRef = FirebaseDatabase.getInstance().getReference("answers");
            answersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot allAnswersSnapshot) {
                    ArrayList<Answer> answers = new ArrayList<>();
                    // Match each answer ID within question to the answers table
                    for (DataSnapshot childSnapshot : answersSnapshot.getChildren()) {
                        String id = childSnapshot.getValue(String.class);
                        Answer answer = allAnswersSnapshot.child(id).getValue(Answer.class);
                        answers.add(answer);
                    }

                    Question q = new Question(question, category, answers);
                    allQuestions.add(q);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}