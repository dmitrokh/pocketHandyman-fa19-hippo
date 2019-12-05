package com.example.pockethandyman;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.HashMap;

public class AnswerQuestionActivity extends AppCompatActivity {
    static final int REQUEST_VIDEO_CAPTURE = 1;

    private TextView questionTextView;
    private Button recordVideoButton;
    private EditText answerField;
    private Button publishButton;
    private BottomNavigationView bottomNavigationView;
    private DatabaseReference dbReference;
    private Question question;
    private Globals globalVars;
    StorageReference storageRef;
    StorageReference videoRef;
    private ProgressBar pBar;
    private String videoFileName = "";
    private Uri videoUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_question);

        globalVars = (Globals) getApplicationContext();

        question = (Question) getIntent().getSerializableExtra("question");

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(false);
//        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.logoColor)));

        questionTextView = findViewById(R.id.questionField);
        recordVideoButton = findViewById(R.id.imageButton2);
        answerField = findViewById(R.id.answerField);
        publishButton = findViewById(R.id.publishButton);
        pBar = findViewById(R.id.progressBar);

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
                if (TextUtils.isEmpty(answerField.getText().toString())
                        || answerField.getText().toString().trim().isEmpty()){
                    Toast.makeText(
                            AnswerQuestionActivity.this, "Enter your answer!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Answer newAnswer = new Answer();
                    newAnswer.author = globalVars.getCurUser();
                    newAnswer.answerText = answerField.getText().toString();
                    newAnswer.questionText = question.getQuestion();

                    newAnswer.videoFileName = videoFileName;

                    HashMap<Integer, Question> allQuestions = globalVars.getAllQuestions();
                    String questionText = question.getQuestion();
                    Question questionBeingAnswered = allQuestions.get(questionText.hashCode());

                    questionBeingAnswered.getAnswers().add(newAnswer);

                    dbReference = FirebaseDatabase.getInstance().getReference("questions");
                    dbReference.child(String.valueOf(questionText.hashCode())).setValue(questionBeingAnswered);

                    Intent intent = new Intent(AnswerQuestionActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            }
        });

        setupBottomNavigationView();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // After camera screen this code will execute
        if (requestCode == REQUEST_VIDEO_CAPTURE ) {

            if (resultCode == RESULT_OK) {
                videoUri = intent.getData();

                // Create a media file name
                // For unique file name appending current timeStamp with file name
                java.util.Date date= new java.util.Date();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date.getTime());
                videoFileName = globalVars.getCurUser() +  "_" + timeStamp;

                storageRef = FirebaseStorage.getInstance().getReference();
                videoRef = storageRef.child("/videos/" + videoFileName);

                uploadData(videoUri);

                // Video captured and saved to fileUri specified in the Intent
                Toast.makeText(AnswerQuestionActivity.this, "Video saved to: " + intent.getData(), Toast.LENGTH_LONG).show();

            } else if (resultCode == RESULT_CANCELED) {

                // User cancelled the video capture
                Toast.makeText(AnswerQuestionActivity.this, "User cancelled the video capture.", Toast.LENGTH_LONG).show();

            } else {
                // Video capture failed, advise user
                Toast.makeText(AnswerQuestionActivity.this, "Video capture failed.", Toast.LENGTH_LONG).show();
            }
        }
    }


    // onOptionsItemSelected is used to go back to previous screen with list of unanswered questions
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }


    private void uploadData(Uri videoUri) {
        //gs://pockethandyman-786ef.appspot.com/videos/dmitro.kh_20191127_114118
        if(videoUri != null){
            UploadTask uploadTask = videoRef.putFile(videoUri);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AnswerQuestionActivity.this,
                            "Upload failed: " + e.getLocalizedMessage(),
                            Toast.LENGTH_LONG).show();

                }
            }).addOnSuccessListener(
                    new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(AnswerQuestionActivity.this, "Upload complete",
                                    Toast.LENGTH_LONG).show();
                        }
            }).addOnProgressListener(
                    new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            updateProgress(taskSnapshot);
                            pBar.setVisibility(View.INVISIBLE);
                        }
                    });
        }else {
            Toast.makeText(AnswerQuestionActivity.this, "Nothing to upload", Toast.LENGTH_SHORT).show();
        }
    }


    public void updateProgress(UploadTask.TaskSnapshot taskSnapshot) {
        @SuppressWarnings("VisibleForTests") long fileSize = taskSnapshot.getTotalByteCount();

        @SuppressWarnings("VisibleForTests")
        long uploadBytes = taskSnapshot.getBytesTransferred();

        long progress = (100 * uploadBytes) / fileSize;

        pBar.setVisibility(View.VISIBLE);
        pBar.setProgress((int) progress);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_repairs_menu, menu);
        setActionBarTitleAndColor();
        return true;
    }

    private void setActionBarTitleAndColor() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            String title = question.getCategory();
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


    private void setupBottomNavigationView() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
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
                        intent = new Intent(AnswerQuestionActivity.this, UserProfileActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
    }


//    public void downloadVideo() {
//
//        try {
//            final File localFile = File.createTempFile(videoFileName, "mp4");
//
//            videoRef.getFile(localFile).addOnSuccessListener(
//                    new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(
//                                FileDownloadTask.TaskSnapshot taskSnapshot) {
//
//                            Toast.makeText(AnswerQuestionActivity.this, "Download complete",
//                                    Toast.LENGTH_LONG).show();
//
//                            final VideoView videoView =
//                                    (VideoView) findViewById(R.id.videoView);
//                            videoView.setVideoURI(Uri.fromFile(localFile));
//                            videoView.start();
//
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(AnswerQuestionActivity.this,
//                            "Download failed: " + e.getLocalizedMessage(),
//                            Toast.LENGTH_LONG).show();
//                }
//            });
//        } catch (Exception e) {
//            Toast.makeText(AnswerQuestionActivity.this,
//                    "Failed to create temp file: " + e.getLocalizedMessage(),
//                    Toast.LENGTH_LONG).show();
//        }
//    }
}
