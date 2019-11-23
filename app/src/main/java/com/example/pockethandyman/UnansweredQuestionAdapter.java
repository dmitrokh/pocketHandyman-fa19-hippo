package com.example.pockethandyman;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UnansweredQuestionAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<Question> questions;
    private Context context;


    public UnansweredQuestionAdapter(Context context,
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
                R.layout.fragment_unanswered_question, viewGroup, false);
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
        holder.title.setText(questions.get(position).getQuestion());
    }
}
