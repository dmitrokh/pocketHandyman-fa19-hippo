package com.example.pockethandyman;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserSearchActivity extends AppCompatActivity {
    private static final String TAG = "ButtonActivity";
    //String taskName = null;
    ViewPager viewPager;
    TabLayout tabLayout;
    ArrayList<String> usersSearchElements;

    private BottomNavigationView bottomNavigationView;
    private Globals globalVars;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_search_activity);

        globalVars = (Globals) getApplicationContext();

        //taskName = getIntent().getStringExtra("ActivityName");
        usersSearchElements = getIntent().getStringArrayListExtra("UsersSearch");

        viewPager = findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabLayout);
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


    // Get all questions for the current category, and split by answered/unanswered
    private HashMap<String, List<Question>> getQuestionsForTask() {
        HashMap<Integer, Question> allQuestions = globalVars.getAllQuestions();

        ArrayList<Question> unansweredQuestions = new ArrayList<>();
        ArrayList<Question> answeredQuestions = new ArrayList<>();

        for (int hashOfQuestion : allQuestions.keySet()) {
            Question question = allQuestions.get(hashOfQuestion);

            for (String elem: usersSearchElements) {
                String tempString = "/0" + question.getQuestion() + "/0";
                //tempString =  "/0" + tempString + "/0";
                if (tempString.contains(elem)) {
                    if (question.getAnswers().size() == 0) {
//                        System.out.println(question.getQuestion());
//                        System.out.println(elem);
                        if (!unansweredQuestions.contains(question)) {
                            unansweredQuestions.add(question);
                        }
                    } else {
                        if (!answeredQuestions.contains(question)) {
//                            System.out.println(question.getQuestion());
//                            System.out.println(elem);
                            answeredQuestions.add(question);
                        }
                    }
                }
            }
        }

        HashMap<String, List<Question>> questions = new HashMap<>();
        questions.put("answered", answeredQuestions);
        questions.put("unanswered", unansweredQuestions);

        return questions;
    }


    //Setting View Pager
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Map<String, List<Question>> questions = getQuestionsForTask();

        adapter.addFrag(new AnsweredQuestionsFragment("Answered questions", null,
                questions.get("answered")), "Answered");
        adapter.addFrag(new UnansweredQuestionsFragment("Unanswered questions", null,
                questions.get("unanswered")), "Unanswered");
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
        setActionBarTitleAndColor("Search Results");

        return true;
    }

    private void setActionBarTitleAndColor(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);

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
                        intent = new Intent(UserSearchActivity.this, AskQuestionActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        break;
                    case R.id.navigation_account:
                        intent = new Intent(UserSearchActivity.this, UserProfileActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
    }

}
