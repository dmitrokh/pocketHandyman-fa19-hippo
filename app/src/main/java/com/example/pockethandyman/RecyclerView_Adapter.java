package com.example.pockethandyman;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerView_Adapter extends RecyclerView.Adapter<ViewHolder> {
    private Context context;
    private ArrayList<Question> questionsForTask;
    private String taskName;


    public RecyclerView_Adapter(Context context,
                                ArrayList<Question> questionsForTask,
                                String taskName) {
        this.context = context;
        this.questionsForTask = questionsForTask;
        this.taskName = taskName;
    }


    @Override
    public int getItemCount() {
        return (null != questionsForTask ? questionsForTask.size() : 0);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ViewHolder mainHolder = (ViewHolder) holder;

        Question curQuestion = questionsForTask.get(position);
        String questionString = curQuestion.getQuestion();

        mainHolder.questionTextView.setText(questionString);
        mainHolder.question = questionString;

        mainHolder.taskName = this.taskName;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.item_unanswered_question, viewGroup, false);
        ViewHolder mainHolder = new ViewHolder(mainGroup) {
            @Override
            public String toString() {
                return super.toString();
            }
        };


        return mainHolder;

    }
}
