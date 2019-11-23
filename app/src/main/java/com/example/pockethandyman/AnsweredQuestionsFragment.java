package com.example.pockethandyman;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AnsweredQuestionsFragment extends Fragment {
    private static final String TAG = "QuestionsListFragment";
    private List<Question> questionsForTask;
    private View view;
    private String title;//String for tab title
    private String taskName;
    private static RecyclerView recyclerView;
    private AnsweredQuestionsAdapter adapter;

    public AnsweredQuestionsFragment(String title, final String taskName, List<Question> questionsForTask) {
        this.title = title; // Setting tab title
        this.taskName = taskName;
        this.questionsForTask = questionsForTask;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.button_activity_fragment, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new AnsweredQuestionsAdapter(getActivity(), questionsForTask);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
