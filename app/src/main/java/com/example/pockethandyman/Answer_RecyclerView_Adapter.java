package com.example.pockethandyman;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Answer_RecyclerView_Adapter extends RecyclerView.Adapter<Answer_ViewHolder> {

    private List<Question> questions;
    private Context context;


    public Answer_RecyclerView_Adapter(Context context, List<Question> questions) {
        this.context = context;
        this.questions = questions;
    }


    @Override
    public int getItemCount() {
        return questions != null ? questions.size() : 0;
    }

    @Override
    public Answer_ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.fragment_answer_item, viewGroup, false);
        Answer_ViewHolder answer_viewHolder = new Answer_ViewHolder(mainGroup) {
            @Override
            public String toString() {
                return super.toString();
            }
        };
        return answer_viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Answer_ViewHolder holder, int position) {
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
