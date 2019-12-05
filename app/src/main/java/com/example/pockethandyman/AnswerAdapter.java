package com.example.pockethandyman;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Adapter to handle answers in the RecyclerView under a question
 */
public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder> {

    private final int MAX_VIDEO_HEIGHT = 1200;
    private final int MAX_VIDEO_WIDTH = 1400;

    private List<Answer> answers;
    private Context context;
    private Globals globalVars;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public static class AnswerViewHolder extends RecyclerView.ViewHolder {

        private TextView author;
        private TextView answerText;
        private VideoView answerVideo;
        private TextView numUpvotes;
        private Button upvote;
        private FrameLayout frame;
        private ImageButton playButton;

        public AnswerViewHolder(View view) {
            super(view);
            author = view.findViewById(R.id.author);
            answerText = view.findViewById(R.id.answerText);
            answerVideo = view.findViewById(R.id.answerVideo);
            numUpvotes = view.findViewById(R.id.numUpvotes);
            upvote = view.findViewById(R.id.upvoteButton);
            frame = view.findViewById(R.id.videoFrame);
            playButton = view.findViewById(R.id.playButton);
        }
    }

    public AnswerAdapter(List<Answer> answers, Context context) {
        this.answers = answers;
        this.context = context;
        this.globalVars = (Globals) context.getApplicationContext();
        this.pref = context.getSharedPreferences("upvotes", Context.MODE_PRIVATE);
        this.editor = this.pref.edit();

        Collections.sort(this.answers, new Comparator<Answer>() {
            @Override
            public int compare(Answer o1, Answer o2) {
                return o2.numUpvotes - o1.numUpvotes;
            }
        });
    }

    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_answer, parent, false);
        return new AnswerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final AnswerViewHolder holder, final int position) {
        final Answer answer = answers.get(position);


        holder.upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set<String> upvotedAnswers = pref.getStringSet(answer.questionText, new HashSet<String>());
                upvotedAnswers = new HashSet<>(upvotedAnswers);
                String positionStr = Integer.toString(position);

                if (upvotedAnswers.contains(positionStr)) {
                    answer.removeUpvote();
                    holder.upvote.setBackground(context.getResources().getDrawable(R.drawable.arrow_lightgray, null));

                    upvotedAnswers.remove(positionStr);
                    editor.putStringSet(answer.questionText, upvotedAnswers);
                    editor.commit();
                } else {
                    answer.addUpvote();
                    holder.upvote.setBackground(context.getResources().getDrawable(R.drawable.arrow_orange, null));

                    upvotedAnswers.add(positionStr);
                    editor.putStringSet(answer.questionText, upvotedAnswers);
                    editor.commit();
                }
                holder.numUpvotes.setText(Integer.toString(answer.numUpvotes));

                // save the updated answer into a question and save updated question into Firebase
                HashMap<Integer, Question> allQuestions = globalVars.getAllQuestions();
                String questionText = answer.questionText;
                Question question = allQuestions.get(questionText.hashCode());

                List<Answer> allAnswersForQuestion = question.getAnswers();

                //find and remove old answer
                Iterator iter = allAnswersForQuestion.iterator();

                while (iter.hasNext()) {
                    Answer answerToCheck = (Answer)iter.next();

                    if (answerToCheck.answerText.equals(answer.answerText)
                            && answerToCheck.author.equals(answer.author)
                            && answerToCheck.videoFileName.equals((answer.videoFileName))) {
                        iter.remove();
                    }
                }

                question.getAnswers().add(answer);

                DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("questions");
                dbReference.child(String.valueOf(questionText.hashCode())).setValue(question);
            }
        });

        holder.author.setText(answer.author + " answered this");
        holder.answerText.setText(answer.answerText);
        holder.numUpvotes.setText(Integer.toString(answer.numUpvotes));

        Set<String> upvotedAnswers = pref.getStringSet(answer.questionText, new HashSet<String>());
        String positionStr = Integer.toString(position);
        if (upvotedAnswers.contains(positionStr)) {
            holder.upvote.setBackground(context.getResources().getDrawable(R.drawable.arrow_orange, null));
        }

        String videoFileName = answer.videoFileName;
        if (videoFileName == null || videoFileName.length() == 0) {
            holder.answerVideo.setVisibility(View.GONE);
            holder.playButton.setVisibility(View.GONE);
            return;
        }

        try {
            final File localFile = File.createTempFile(videoFileName, "mp4");

            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            final StorageReference videoRef = storageRef.child("/videos/" + videoFileName);
            videoRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    final VideoView videoView = holder.answerVideo;
                    videoView.setVideoPath(localFile.getAbsolutePath());
                    Log.e("adapter", "set file uri");

                    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(final MediaPlayer mp) {
                            double videoWidth = mp.getVideoWidth();
                            double videoHeight = mp.getVideoHeight();
                            int displayWidth, displayHeight;
                            if (videoWidth > videoHeight) {
                                displayWidth = MAX_VIDEO_WIDTH;
                                displayHeight = (int) (videoHeight / videoWidth * displayWidth);
                            } else {
                                displayHeight = MAX_VIDEO_HEIGHT;
                                displayWidth = (int) (videoWidth / videoHeight * displayHeight);
                            }

                            ViewGroup.LayoutParams layout = videoView.getLayoutParams();
                            layout.width = displayWidth;
                            layout.height = displayHeight;
                            videoView.setLayoutParams(layout);

                            holder.frame.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ImageButton playButton = holder.playButton;
                                    if (mp.isPlaying()) {
                                        playButton.setVisibility(View.VISIBLE);
                                        videoView.pause();
                                    } else {
                                        playButton.setVisibility(View.GONE);
                                        videoView.start();
                                    }
                                }
                            });
                        }
                    });

                    videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            holder.playButton.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("adapter", "download failed");
                    Toast.makeText(context, "Download failed: " + e.getLocalizedMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            Log.e("adapter", "failed to create temp file");
            Toast.makeText(context, "Failed to create temp file: " + e.getLocalizedMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return answers.size();
    }
}
