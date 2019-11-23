package com.example.pockethandyman;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UnansweredQuestionsAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<Question> questions;
    private Context context;


    public UnansweredQuestionsAdapter(Context context,
                                      List<Question> questions) {
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
                R.layout.item_question, viewGroup, false);

        ViewHolder mainHolder = new ViewHolder(mainGroup) {
            @Override
            public String toString() {
                return super.toString();
            }
        };
        return mainHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Question question = questions.get(position);
        holder.title.setText(question.getQuestion());

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
