package com.example.pockethandyman;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class AnswerQuestionActivity extends AppCompatActivity {
    static final int REQUEST_VIDEO_CAPTURE = 1;

    private TextView questionTextView;
    private ImageButton recordVideoButton;
    private EditText answerField;
    private Button publishButton;
    private BottomNavigationView bottomNavigationView;
    private DatabaseReference dbReference;
    private Question question;
    private Globals globalVars;
    StorageReference storageRef;
    StorageReference videoRef;
    private ProgressBar pBar;
    private String videoFileName;
    private Uri videoUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_question);

        globalVars = (Globals) getApplicationContext();

        question = (Question) getIntent().getSerializableExtra("question");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.logoColor)));

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
                if (TextUtils.isEmpty(answerField.getText().toString())){
                    Toast.makeText(
                            AnswerQuestionActivity.this, "Enter your answer!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Answer newAnswer = new Answer();
                    newAnswer.author = globalVars.getCurUser();
                    newAnswer.answerText = answerField.getText().toString();

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
        setActionBar(question);
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


    private void setActionBar(Question question) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(question.getCategory());
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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
