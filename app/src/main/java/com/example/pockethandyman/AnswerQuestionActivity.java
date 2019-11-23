package com.example.pockethandyman;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnswerQuestionActivity extends AppCompatActivity {
    private TextView questionTextView;
    private ImageButton recordVideo;
    private EditText answerField;
    private Button publishButton;
    private BottomNavigationView bottomNavigationView;
    private DatabaseReference dbReference;
    private String question;
    private Globals globalVars;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_question);

        globalVars = (Globals) getApplicationContext();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.logoColor)));

        questionTextView = findViewById(R.id.questionField);
        recordVideo = findViewById(R.id.imageButton2);
        answerField = findViewById(R.id.answerField);

        question = getIntent().getStringExtra("Question");

        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Answer newAnswer = new Answer();

                HashMap<Integer, Question> allQuestions = globalVars.getAllQuestions();
                Question questionBeingAnswered = allQuestions.get(question.hashCode());

                questionBeingAnswered.getAnswers().add(newAnswer);

                dbReference = FirebaseDatabase.getInstance().getReference("questions");
                dbReference.child(String.valueOf(question.hashCode())).setValue(questionBeingAnswered);

//                Intent intent = new Intent(AskQuestionActivity.this, EmailPasswordActivity.class);
//                startActivity(intent);
            }
        });

        setupBottomNavigationView();
    }


    private void setupBottomNavigationView() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_question);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent = null;
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        intent = new Intent(AnswerQuestionActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        break;
                    case R.id.navigation_question:
                        intent = new Intent(AnswerQuestionActivity.this, AskQuestionActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        break;
                    case R.id.navigation_account:
                        // TODO: implement account activity
//                        intent = new Intent(HomeActivity.this, AccountAcitvity.class);
//                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
    }
}
