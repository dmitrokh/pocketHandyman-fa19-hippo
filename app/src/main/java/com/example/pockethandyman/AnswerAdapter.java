package com.example.pockethandyman;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder> {

    private List<Answer> answers;

    public static class AnswerViewHolder extends RecyclerView.ViewHolder {

        private TextView author;
        private TextView answerText;

        public AnswerViewHolder(View view) {
            super(view);
            author = view.findViewById(R.id.author);
            answerText = view.findViewById(R.id.answerText);
        }
    }

    public AnswerAdapter(List<Answer> answers) {
        this.answers = answers;
    }

    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_answer, parent, false);
        return new AnswerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerViewHolder holder, int position) {
        Answer answer = answers.get(position);
        holder.author.setText(answer.author);
        holder.answerText.setText(answer.answerText);
    }

    @Override
    public int getItemCount() {
        return answers.size();
    }
}
