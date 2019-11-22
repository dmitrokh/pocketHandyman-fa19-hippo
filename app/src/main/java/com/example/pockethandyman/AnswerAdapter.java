package com.example.pockethandyman;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder> {
    public static class AnswerViewHolder extends RecyclerView.ViewHolder {
        public AnswerViewHolder(View view) {
            super(view);
        }
    }

    public AnswerAdapter() {

    }

    @Override
    public AnswerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_answer, parent, false);
        return new AnswerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
