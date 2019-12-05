package com.example.pockethandyman;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for asking a question
 */
public class AskQuestionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private String categoryChosen;
    private BottomNavigationView bottomNavigationView;
    private EditText questionField;
    private Button publishButton;
    private Spinner chooseCategory;
    private DatabaseReference dbReference;
    private ChipGroup chipGroup;
    private EditText tagField;
    private Globals globalVars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);

        globalVars = (Globals) getApplicationContext();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.logoColor)));

        chooseCategory = findViewById(R.id.spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.rubrik_array,
                R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        chooseCategory.setAdapter(adapter);
        chooseCategory.setOnItemSelectedListener(this);

        chipGroup = findViewById(R.id.chipGroup);
        tagField = (EditText) findViewById(R.id.tagField);

        tagField.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.toString().trim().length() > 2 && s.charAt(s.length() - 1) == ',') {
                    Chip chip = new Chip(AskQuestionActivity.this);
                    chip.setChipText("#" + s.subSequence(0, s.length() - 1));
                    chip.setTextAppearance(R.style.ChipTextStyle);
                    chipGroup.addView(chip);
                    tagField.setText("");
                } else if (s.toString().trim().length() == 1 && s.charAt(0) == ',') {
                    tagField.setText("");
                }
            }
        });

        questionField = findViewById(R.id.questionField);
        publishButton = findViewById(R.id.publishButton);
        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooseCategory.getSelectedItem().toString().isEmpty()
                        || chooseCategory.getSelectedItem().toString().equals("No selection")) {
                    TextView errorText = (TextView)chooseCategory.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);
                    errorText.setText("Choose category");

                    return;
                }

                if (TextUtils.isEmpty(questionField.getText().toString())
                        || questionField.getText().toString().trim().isEmpty()){
                    Toast.makeText(
                            AskQuestionActivity.this, "Enter your question!",
                            Toast.LENGTH_SHORT).show();

                    return;
                } else {
                    String question = questionField.getText().toString();
                    int hash = question.hashCode();
//                    String hashString = String.valueOf(hash);

                    String userName = globalVars.getCurUser();

                    Question toAsk = new Question(question, categoryChosen, userName);

                    List<String> tags = new ArrayList<>();
                    for (int i = 0; i < chipGroup.getChildCount(); i++) {
                        tags.add(((Chip) chipGroup.getChildAt(i)).getText().toString());
                    }
                    toAsk.setTags(tags);

                    dbReference = FirebaseDatabase.getInstance().getReference("questions");
                    dbReference.child(String.valueOf(hash)).setValue(toAsk);

                    Toast.makeText(AskQuestionActivity.this, "Question published!",
                            Toast.LENGTH_LONG).show();
                }

                Intent intent = new Intent(AskQuestionActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        setupBottomNavigationView();



        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);
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
                        intent = new Intent(AskQuestionActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        break;
                    case R.id.navigation_question:
                        // do nothing: you're already at ask question
                        break;
                    case R.id.navigation_account:
                        intent = new Intent(AskQuestionActivity.this, UserProfileActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View v, int position, long id) {
        categoryChosen = adapterView.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
