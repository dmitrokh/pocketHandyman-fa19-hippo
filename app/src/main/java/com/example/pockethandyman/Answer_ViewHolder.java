package com.example.pockethandyman;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Answer_ViewHolder extends RecyclerView.ViewHolder {

    public TextView title;


    public Answer_ViewHolder(@NonNull View itemView) {
        super(itemView);

        this.title = (TextView) itemView.findViewById(R.id.cardTitle2);



    }
}
