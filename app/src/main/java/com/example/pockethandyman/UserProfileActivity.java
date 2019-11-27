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

public class UserProfileActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private EditText screenNameText;
    private String screenName;
    private TextView welcome;
    private Button signOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.logoColor)));

        screenNameText = (EditText) findViewById(R.id.nameEditText);
        welcome = (TextView) findViewById(R.id.welcome);
        signOutButton = (Button) findViewById(R.id.signOutButton);


        screenNameText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                screenName = s.toString();
                if (s.length() > 0) {
                    welcome.setText("Welcome, " + screenName + "!");
                    welcome.setVisibility(View.VISIBLE);
                } else {
                    welcome.setText("");
                    welcome.setVisibility(View.INVISIBLE);
                }

        }});

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

}
