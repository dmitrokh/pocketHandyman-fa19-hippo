package com.example.pockethandyman;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private EditText screenNameText;
    private String screenName;
    private TextView welcome;
    private TextView questionsAskedByUser;
    private TextView questionsAnsweredByUser;
    private Button signOutButton;
    private Globals globalVars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        globalVars = (Globals) getApplicationContext();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.logoColor)));

        //screenNameText = (EditText) findViewById(R.id.nameEditText);
        welcome = (TextView) findViewById(R.id.welcome);
        questionsAskedByUser = (TextView) findViewById(R.id.contributions_text1);
        questionsAnsweredByUser = (TextView) findViewById(R.id.contributions_text2);
        signOutButton = (Button) findViewById(R.id.signOutButton);

        int[] contributions = getUsersContributions();
        questionsAskedByUser.setText(Integer.toString(contributions[0]) + " Questions Asked");
        questionsAnsweredByUser.setText(Integer.toString(contributions[1]) + " Questions Answered");

//        screenNameText.addTextChangedListener(new TextWatcher() {
//
//            public void afterTextChanged(Editable s) {}
//
//            public void beforeTextChanged(CharSequence s, int start,
//                                          int count, int after) {
//            }
//
//            public void onTextChanged(CharSequence s, int start,
//                                      int before, int count) {
//                screenName = s.toString();
//                if (s.length() > 0) {
//                    welcome.setText("Welcome, " + screenName + "!");
//                    welcome.setVisibility(View.VISIBLE);
//                } else {
//                    welcome.setText("");
//                    welcome.setVisibility(View.INVISIBLE);
//                }
//
//            }});

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmailPasswordActivity.getInstance().signOut();
                Intent intent = new Intent(UserProfileActivity.this, EmailPasswordActivity.class);
                startActivity(intent);
            }
        });


        setupBottomNavigationView();
    }

    private void setupBottomNavigationView() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_account);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent = null;
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        intent = new Intent(UserProfileActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        break;
                    case R.id.navigation_question:
                        intent = new Intent(UserProfileActivity.this, AskQuestionActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        break;
                    case R.id.navigation_account:

                        break;
                }
                return true;
            }
        });
    }

    private int[] getUsersContributions() {
        String curUser = globalVars.getCurUser();
        // array to store the user's contributions: first value - questions asked, second - questions answered
        int[] contributions = new int[2];

        //if user is null, return empty array
        if (curUser == null) {
            return contributions;
        }

        HashMap<Integer, Question> allQuestiions = globalVars.getAllQuestions();

        if (allQuestiions != null && !allQuestiions.isEmpty()) {
            for (Integer hashOfQuestion : allQuestiions.keySet()) {
                Question question = allQuestiions.get(hashOfQuestion);

                if (question.getAuthor() != null && question.getAuthor().equals(curUser)) {
                    contributions[0]++;
                }

                List<Answer> answers = question.getAnswers();

                for (Answer answer : answers) {
                    if (answer.author != null && answer.author.equals(curUser)) {
                        contributions[1]++;
                    }
                }
            }
        }

        return contributions;
    }
}
