package com.example.pockethandyman;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class AskQuestionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private String categoryChosen;
    private BottomNavigationView bottomNavigationView;
    private EditText questionField;
    private Button publishButton;
    private Spinner chooseCategory;
    private DatabaseReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);

//        ActionBar actionBar = getActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);

        chooseCategory = findViewById(R.id.spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.rubrik_array,
                R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        chooseCategory.setAdapter(adapter);
        chooseCategory.setOnItemSelectedListener(this);

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

                if (TextUtils.isEmpty(questionField.getText().toString())){
                    Toast.makeText(
                            AskQuestionActivity.this, "Enter your questionField or go back!",
                    Toast.LENGTH_SHORT).show();
                } else {
                    String question = questionField.getText().toString();
                    int hash = question.hashCode();
//                    String hashString = String.valueOf(hash);
                    Question toAsk = new Question(question, categoryChosen);

                    dbReference = FirebaseDatabase.getInstance().getReference("questions");
                    dbReference.child(String.valueOf(hash)).setValue(toAsk);
                }

                Intent intent = new Intent(AskQuestionActivity.this, EmailPasswordActivity.class);
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
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent = null;
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        intent = new Intent(AskQuestionActivity.this, HomeActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.navigation_question:
                        // do nothing: you're already at ask question
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


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View v, int position, long id) {
        categoryChosen = adapterView.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
