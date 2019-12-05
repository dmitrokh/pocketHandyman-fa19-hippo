package com.example.pockethandyman;

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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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

/**
 * Activity that handles displaying the questions for a specified category
 */
public class ButtonActivity extends AppCompatActivity {
    private static final String TAG = "ButtonActivity";
    String taskName = null;
    ViewPager viewPager;
    TabLayout tabLayout;

    private BottomNavigationView bottomNavigationView;
    private Globals globalVars;

    private EditText searchBoxEntry;
    private String searchBoxEntryText;
    private String[] textEntryArray;
    private ArrayList<String> textEntryElements = new ArrayList<>();
    private ViewPagerAdapter adapter;
    private ImageView magnifying_glass;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.button_activity_layout);

        globalVars = (Globals) getApplicationContext();

        taskName = getIntent().getStringExtra("ActivityName");

        if (getIntent().hasExtra("UsersSearch")) {
            textEntryElements = getIntent().getStringArrayListExtra("UsersSearch");
        }

        viewPager = findViewById(R.id.viewPager);
        setupViewPager();

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



        magnifying_glass = (ImageView) findViewById(R.id.magnifying_glass);
        magnifying_glass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchBoxEntry = (EditText) findViewById(R.id.editText2);
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

                //System.out.println(textEntryElements);
                //setupViewPagerAfterUserSearch();

                Intent intent = new Intent(view.getContext(), ButtonActivity.class);
                intent.putExtra("UsersSearch", textEntryElements);
                intent.putExtra("ActivityName", taskName);
                startActivity(intent);
            }
        });
    }


    // Get all questions for the current category, and split by answered/unanswered
    private HashMap<String, List<Question>> getQuestionsForTask() {
        HashMap<Integer, Question> allQuestions = globalVars.getAllQuestions();

        ArrayList<Question> unansweredQuestionsFull = new ArrayList<>();
        ArrayList<Question> answeredQuestionsFull = new ArrayList<>();

//        ArrayList<Question> unansweredQuestionsUserSearch = new ArrayList<>();
//        ArrayList<Question> answeredQuestionsUserSearch = new ArrayList<>();

        for (int hashOfQuestion : allQuestions.keySet()) {
            Question question = allQuestions.get(hashOfQuestion);

            if (question.getCategory().equals(taskName)) {
                if (textEntryElements.isEmpty()) {
                    if (question.getAnswers().size() == 0) {
                        unansweredQuestionsFull.add(question);
                    } else {
                        answeredQuestionsFull.add(question);
                    }

                } else {
                    String tempString = "/0" + question.getQuestion() + "/0";
                    for (String elem : textEntryElements) {
                        if (tempString.contains(elem)) {
                            if (question.getAnswers().size() == 0) {
                                if (!unansweredQuestionsFull.contains(question)) {
                                    unansweredQuestionsFull.add(question);
                                }
                            } else {
                                if (!answeredQuestionsFull.contains(question)) {
                                    answeredQuestionsFull.add(question);
                                }
                            }
                        }
                    }
                }
            }
        }

        HashMap<String, List<Question>> questions = new HashMap<>();
        questions.put("answered", answeredQuestionsFull);
        questions.put("unanswered", unansweredQuestionsFull);

//        questions.put("answeredUserSearch", answeredQuestionsUserSearch);
//        questions.put("unansweredUserSearch", unansweredQuestionsUserSearch);


        return questions;
    }

//    private HashMap<String, List<Question>> getQuestionsForTaskUserSearch() {
//        HashMap<Integer, Question> allQuestions = globalVars.getAllQuestions();
//
//        ArrayList<Question> unansweredQuestions = new ArrayList<>();
//        ArrayList<Question> answeredQuestions = new ArrayList<>();
//
//        for (int hashOfQuestion : allQuestions.keySet()) {
//            Question question = allQuestions.get(hashOfQuestion);
//
//            for (String elem: textEntryElements) {
//                if (question.getQuestion().contains(elem)) {
//                    if (question.getAnswers().size() == 0) {
//                        unansweredQuestions.add(question);
//                    } else {
//                        answeredQuestions.add(question);
//                    }
//                }
//            }
//        }
//
//        HashMap<String, List<Question>> questions = new HashMap<>();
//        questions.put("answered", answeredQuestions);
//        questions.put("unanswered", unansweredQuestions);
//
//        return questions;
//    }


    //Setting View Pager
    private void setupViewPager() {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Map<String, List<Question>> questions = getQuestionsForTask();

        adapter.addFrag(new AnsweredQuestionsFragment("Answered questions", taskName,
                questions.get("answered")), "Answered");
        adapter.addFrag(new UnansweredQuestionsFragment("Unanswered questions", taskName,
                questions.get("unanswered")), "Unanswered");

        viewPager.setAdapter(adapter);
    }

//    //Setting View Pager
//    private void setupViewPagerAfterUserSearch() {
//        adapter.removeAllFragments();
//        //adapter = new ViewPagerAdapter(getSupportFragmentManager());
//
//        Map<String, List<Question>> questions = getQuestionsForTask();
//
//        System.out.println(questions.get("answeredUserSearch"));
//
//        adapter.addFrag(new AnsweredQuestionsFragment("Answered questions", taskName,
//                questions.get("answeredUserSearch")), "Answered");
//        adapter.addFrag(new UnansweredQuestionsFragment("Unanswered questions", taskName,
//                questions.get("unansweredUserSearch")), "Unanswered");
//        //adapter.notifyDataSetChanged();
//        //viewPager.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
//
//    }

    //View Pager fragments setting adapter class
    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragmentList = new ArrayList<>();//fragment arraylist
        private List<String> mFragmentTitleList = new ArrayList<>();//title arraylist

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


//        public void removeAllFragments () {
//            mFragmentTitleList = new ArrayList<>();
//            mFragmentList = new ArrayList<>();
//            notifyDataSetChanged();
//        }

    }



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
                        intent = new Intent(ButtonActivity.this, UserProfileActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER:
                searchBoxEntry = (EditText) findViewById(R.id.editText2);
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

                //System.out.println(textEntryElements);
               //setupViewPagerAfterUserSearch();

                Intent intent = new Intent(this, ButtonActivity.class);
                intent.putExtra("UsersSearch", textEntryElements);
                intent.putExtra("ActivityName", taskName);
                startActivity(intent);



                return true;


            default:
                return super.onKeyUp(keyCode, event);
        }
    }
}
