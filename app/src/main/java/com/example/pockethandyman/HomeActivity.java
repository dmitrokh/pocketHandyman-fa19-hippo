package com.example.pockethandyman;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    Integer buttonSelector = null;
    private BottomNavigationView bottomNavigationView;
    private Globals globalVars;
    private DatabaseReference dbRef;
    private HashMap<Integer, Question> allQuestions = new HashMap<>();
    private EditText searchBoxEntry;
    private String searchBoxEntryText;
    private String[] textEntryArray;
    private ArrayList<String> textEntryElements;
    private ImageView magnifying_glass;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        globalVars = (Globals) getApplicationContext();

        magnifying_glass = (ImageView) findViewById(R.id.magnifying_glass);
        magnifying_glass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchBoxEntry = (EditText) findViewById(R.id.editText);
                searchBoxEntryText = searchBoxEntry.getText().toString();
                textEntryArray = searchBoxEntryText.split(" ");
                textEntryElements = new ArrayList<>();
                for (String elem: textEntryArray) {
                    if (elem.toLowerCase().equals("how") || elem.toLowerCase().equals("the") || elem.toLowerCase().equals("a") ||
                            elem.toLowerCase().equals("to") || elem.toLowerCase().equals("fix") || elem.toLowerCase().equals("repair") ||
                            elem.toLowerCase().equals("replace")) {

                        // Do nothing because we do not want to count these words
                        //Could add more but this is a start
                    } else {
                        textEntryElements.add(" " + elem + " ");
                        textEntryElements.add(" " + elem + "/0");
                        textEntryElements.add("/0" + elem + " ");
                        textEntryElements.add(elem + ".");
                        textEntryElements.add(elem + "?");
                        textEntryElements.add(elem + "!");

                    }
                }

                Intent intent = new Intent(view.getContext(), UserSearchActivity.class);
                intent.putExtra("UsersSearch", textEntryElements);
                startActivity(intent);
            }
        });



        dbRef = FirebaseDatabase.getInstance().getReference().child("questions");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String question = "",
                            category = "";

                    ArrayList<Answer> answers = new ArrayList<>();
                    ArrayList<String> tags = new ArrayList<>();

                    DataSnapshot questionSnapshot = ds.child("question");
                    question = (String)questionSnapshot.getValue();

                    DataSnapshot categorySnapshot = ds.child("category");
                    category = (String)categorySnapshot.getValue();

                    String author = ds.child("author").getValue(String.class);

                    Question q = new Question(question, category, author);

                    if (ds.hasChild("answers")) {
                        DataSnapshot answersSnapshot = ds.child("answers");
                        // Get each answer from the question
                        for (DataSnapshot childSnapshot : answersSnapshot.getChildren()) {
                            Answer answer = childSnapshot.getValue(Answer.class);
                            answers.add(answer);
                        }
//                        DataSnapshot answersSnapshot = ds.child("answers");
//                        answers = (ArrayList<Answer>)answersSnapshot.getValue();
//                        q.setAnswers(answers);
                    }
                    q.setAnswers(answers);

                    if (ds.hasChild("tags")) {
                        DataSnapshot tagsSnapshot = ds.child("tags");
                        tags = (ArrayList<String>)tagsSnapshot.getValue();
                    }
                    q.setTags(tags);

                    int hashOfQuestionString = question.hashCode();
                    allQuestions.put(hashOfQuestionString, q);

//                    Log.d(TAG, "question: " + question);
                }

                globalVars.setAllQuestions(allQuestions);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Button homeAppliances = (Button) findViewById(R.id.Home_appliance_button);
        homeAppliances.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonSelector = 0;
                OpenButtonActivity();
            }
        });

        Button officeElectronics = (Button) findViewById(R.id.Office_electronics);
        officeElectronics.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonSelector = 1;
                OpenButtonActivity();
            }
        });

        Button automotive = (Button) findViewById(R.id.Automotive);
        automotive.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonSelector = 2;
                OpenButtonActivity();
            }
        });

        Button homeRepairs = (Button) findViewById(R.id.Home_Repairs);
        homeRepairs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonSelector = 3;
                OpenButtonActivity();
            }
        });

        setupBottomNavigationView();




    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER:
                searchBoxEntry = (EditText) findViewById(R.id.editText);
                searchBoxEntryText = searchBoxEntry.getText().toString();
                textEntryArray = searchBoxEntryText.split(" ");
                textEntryElements = new ArrayList<>();
                for (String elem: textEntryArray) {
                    if (elem.toLowerCase().equals("how") || elem.toLowerCase().equals("the") || elem.toLowerCase().equals("a") ||
                            elem.toLowerCase().equals("to") || elem.toLowerCase().equals("fix") || elem.toLowerCase().equals("repair") ||
                            elem.toLowerCase().equals("replace")) {

                        // Do nothing because we do not want to count these words
                        //Could add more but this is a start
                    } else {
                        textEntryElements.add(" " + elem + " ");
                        textEntryElements.add(" " + elem + "/0");
                        textEntryElements.add("/0" + elem + " ");
                        textEntryElements.add(elem + ".");
                        textEntryElements.add(elem + "?");
                        textEntryElements.add(elem + "!");

                    }
                }

                Intent intent = new Intent(this, UserSearchActivity.class);
                intent.putExtra("UsersSearch", textEntryElements);
                startActivity(intent);

                //System.out.println(textEntryElements);
                return true;


            default:
                return super.onKeyUp(keyCode, event);
        }
    }




    public void OpenButtonActivity() {
        Intent intent = new Intent(this, ButtonActivity.class);

        if (buttonSelector == 0) {
            intent.putExtra("ActivityName", "Home Appliances");
        }
        else if (buttonSelector == 1) {
            intent.putExtra("ActivityName", "Office Electronics");
        }
         else if (buttonSelector == 2) {
            intent.putExtra("ActivityName", "Automotive");
        }
         else {
            intent.putExtra("ActivityName", "Home Repairs");
        }

        startActivity(intent);


    }


    private void setupBottomNavigationView() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent = null;
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        // do nothing: you're already at home
                        break;
                    case R.id.navigation_question:
                        intent = new Intent(HomeActivity.this, AskQuestionActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        break;
                    case R.id.navigation_account:
                        intent = new Intent(HomeActivity.this, UserProfileActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
    }
}
