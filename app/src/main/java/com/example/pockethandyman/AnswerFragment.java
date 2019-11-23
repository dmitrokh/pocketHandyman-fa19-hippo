package com.example.pockethandyman;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AnswerFragment extends Fragment {
    private static final String TAG = "QuestionsFragment";
    private final ArrayList<String> answerTitles;
    private View view;
    private String title;//String for tab title
    private String taskName;
    private static RecyclerView recyclerView;
    private Answer_RecyclerView_Adapter adapter;

    public AnswerFragment(String title, final String taskName, ArrayList<String> answerTitles) {
        this.title = title; // Setting tab title
        this.taskName = taskName;
        this.answerTitles = answerTitles;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.button_activity_fragment, container, false);

        setRecyclerView();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }


    private void setRecyclerView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new Answer_RecyclerView_Adapter(getActivity(), answerTitles);
        recyclerView.setAdapter(adapter);
    }
}
