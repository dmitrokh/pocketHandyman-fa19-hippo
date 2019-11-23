package com.example.pockethandyman;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ButtonActivity extends AppCompatActivity {
    private static final String TAG = "ButtonActivity";
    String taskName = null;
    ViewPager viewPager;
    TabLayout tabLayout;

    private BottomNavigationView bottomNavigationView;
    private Globals globalVars;
    private ArrayList<Question> questionsForTask = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.button_activity_layout);

        globalVars = (Globals) getApplicationContext();

        taskName = getIntent().getStringExtra("ActivityName");

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);//setting tab over viewpager

        //Implementing tab selected listener over tablayout
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());//setting current selected item over viewpager
                switch (tab.getPosition()) {
                    case 0:
                        Log.e("TAG","TAB1");
                        break;
                    case 1:
                        Log.e("TAG","TAB2");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        setupBottomNavigationView();
    }


    private void getQuestionsForTask() {
        HashMap<Integer, Question> allQuestions = globalVars.getAllQuestions();

        for (Integer hash : allQuestions.keySet()) {
            Question question = allQuestions.get(hash);

            if (question.getCategory().equals(taskName)) {
                questionsForTask.add(question);
            }
        }
    }


    //Setting View Pager
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        getQuestionsForTask();

        adapter.addFrag(new AnswerFragment("Unanswered questions", taskName, questionsForTask), "Unanswered");
        adapter.addFrag(new AnswerFragment("Answered questions", taskName, questionsForTask), "Answered");
        viewPager.setAdapter(adapter);
    }

    //View Pager fragments setting adapter class
    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();//fragment arraylist
        private final List<String> mFragmentTitleList = new ArrayList<>();//title arraylist

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }


        //adding fragments and title method
        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_repairs_menu, menu);
        if (taskName != null) {
            setActionBarTitle(taskName);
        }
        return true;
    }

    private void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                        intent = new Intent(ButtonActivity.this, AskQuestionActivity.class);
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
