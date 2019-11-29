package com.example.pockethandyman;

import android.content.Context;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.List;

/**
 * Adapter to handle answers in the RecyclerView under a question
 */
public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder> {

    private final int MAX_VIDEO_HEIGHT = 600;
    private final int MAX_VIDEO_WIDTH = 600;

    private List<Answer> answers;
    private Context context;

    public static class AnswerViewHolder extends RecyclerView.ViewHolder {

        private TextView author;
        private TextView answerText;
        private VideoView answerVideo;

        public AnswerViewHolder(View view) {
            super(view);
            author = view.findViewById(R.id.author);
            answerText = view.findViewById(R.id.answerText);
            answerVideo = view.findViewById(R.id.answerVideo);
        }
    }

    public AnswerAdapter(List<Answer> answers, Context context) {
        this.answers = answers;
        this.context = context;
    }

    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_answer, parent, false);
        return new AnswerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final AnswerViewHolder holder, int position) {
        Answer answer = answers.get(position);
        holder.author.setText(answer.author);
        holder.answerText.setText(answer.answerText);

        String videoFileName = answer.videoFileName;
        if (videoFileName == null || videoFileName.length() == 0) {
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
                        public void onPrepared(MediaPlayer mp) {
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

                            videoView.start();
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
