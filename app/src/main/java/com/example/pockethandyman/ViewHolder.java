package com.example.pockethandyman;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ViewHolder extends RecyclerView.ViewHolder {


    public TextView title;
    public Button answerButton;


    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        this.title = (TextView) itemView.findViewById(R.id.cardTitle);
        this.answerButton = itemView.findViewById(R.id.answerButton);

        answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO: implement answer questions logic here
//                Intent intent = new Intent (v.getContext(), GoToSomewhere.class);
//                v.getContext().startActivity(intent);
            }
        });

    }



}

