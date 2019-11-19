package com.example.pockethandyman;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {


    public TextView title;


    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        this.title = (TextView) itemView.findViewById(R.id.cardTitle);
    }



}

