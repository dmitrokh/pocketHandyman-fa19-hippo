package com.example.pockethandyman;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Adapter that handles displaying answered questions in a RecyclerView
 */
public class AnsweredQuestionsAdapter extends RecyclerView.Adapter<AnsweredQuestionsAdapter.ViewHolder> {

    private List<Question> questions;
    private Context context;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView author;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.questionTitle);
            this.author = itemView.findViewById(R.id.author);
        }
    }


    public AnsweredQuestionsAdapter(Context context, List<Question> questions) {
        this.context = context;
        this.questions = questions;
    }

    @Override
    public int getItemCount() {
        return questions != null ? questions.size() : 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.item_answered_question, viewGroup, false);
        return new ViewHolder(mainGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Question question = questions.get(position);
        Log.e(this.getClass().getName(), question.getQuestion() + " " + question.getAuthor());
        holder.title.setText(question.getQuestion());
        holder.author.setText(question.getAuthor());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AnswerActivity.class);
                intent.putExtra("question", question);
                context.startActivity(intent);
            }
        });
    }
}
