package com.example.pockethandyman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.List;

/**
 * Activity to view a question and its answers
 */
public class AnswerActivity extends AppCompatActivity {

    private String taskName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answered_question);

        final RecyclerView answersRecyclerView = findViewById(R.id.answers);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        answersRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                answersRecyclerView.getContext(), layoutManager.getOrientation());
        answersRecyclerView.addItemDecoration(dividerItemDecoration);

        Question question = (Question) getIntent().getSerializableExtra("question");
        List<Answer> answers = question.getAnswers();
        RecyclerView.Adapter answerAdapter = new AnswerAdapter(answers, this);
        answersRecyclerView.setAdapter(answerAdapter);

        TextView questionTitle = findViewById(R.id.questionTitle);
        questionTitle.setText(question.getQuestion());
        TextView author = findViewById(R.id.author);
        author.setText(question.getAuthor());

        taskName = question.getCategory();

        setupBottomNavigationView();
    }

//    private void setActionBar(Question question) {
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setTitle(question.getCategory());
//            getSupportActionBar().setHomeButtonEnabled(true);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_repairs_menu, menu);
        if (taskName != null) {
            setActionBarTitleAndColor(taskName);
        }
        return true;
    }

    private void setActionBarTitleAndColor(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);

            Spannable text = new SpannableString(actionBar.getTitle());
            text.setSpan(new ForegroundColorSpan(Color.WHITE), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            actionBar.setTitle(text);

            Drawable backArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
            backArrow.setColorFilter(getResources().getColor(R.color.White), PorterDuff.Mode.SRC_ATOP);
            actionBar.setHomeAsUpIndicator(backArrow);

            switch (title) {
                case "Home Appliances":
                    actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.HomeAppliancesColor)));
                    break;
                case "Office Electronics":
                    actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.OfficeElectronicsColor)));
                    break;
                case "Automotive":
                    actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.AutomotiveColor)));
                    break;
                case "Home Repairs":
                    actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.HomeRepairsColor)));
                    break;
                default:
                    actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.logoColor)));
                    break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    private void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
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
                        intent = new Intent(AnswerActivity.this, AskQuestionActivity.class);
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
