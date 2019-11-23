package com.example.pockethandyman;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {


    public TextView title;
    public Button answerButton;


    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        this.title = itemView.findViewById(R.id.cardTitle);
    }
}

