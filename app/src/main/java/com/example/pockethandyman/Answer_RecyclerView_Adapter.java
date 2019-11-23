package com.example.pockethandyman;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Answer_RecyclerView_Adapter extends RecyclerView.Adapter<Answer_ViewHolder> {

    private ArrayList<String> arrayList;
    private Context context;


    public Answer_RecyclerView_Adapter(Context context,
                                ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;

    }


    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);


    }

    @Override
    public void onBindViewHolder(@NonNull Answer_ViewHolder holder, int position) {
        final Answer_ViewHolder mainHolder = (Answer_ViewHolder)holder;
        mainHolder.title.setText(arrayList.get(position));
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


}
