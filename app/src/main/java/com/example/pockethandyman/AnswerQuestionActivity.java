package com.example.pockethandyman;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import io.opencensus.internal.Utils;

public class AnswerQuestionActivity extends AppCompatActivity {
    static final int REQUEST_VIDEO_CAPTURE = 1;

    private TextView questionTextView;
    private ImageButton recordVideoButton;
    private EditText answerField;
    private Button publishButton;
    private BottomNavigationView bottomNavigationView;
    private DatabaseReference dbReference;
    private Question question;
    private Globals globalVars;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_question);

        globalVars = (Globals) getApplicationContext();
        question = (Question) getIntent().getSerializableExtra("question");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.logoColor)));

        questionTextView = findViewById(R.id.questionField);
        recordVideoButton = findViewById(R.id.imageButton2);
        answerField = findViewById(R.id.answerField);
        publishButton = findViewById(R.id.publishButton);

        questionTextView.setText(question.getQuestion());

        recordVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
                }
            }
        });

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
        setActionBar(question);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = intent.getData();

            System.out.println();
        }
    }


    private void setActionBar(Question question) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(question.getCategory());
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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
