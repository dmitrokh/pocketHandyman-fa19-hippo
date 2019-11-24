package com.example.pockethandyman;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UnansweredQuestionsFragment extends Fragment {
    private static final String TAG = "UnansweredQuestionsFragment";
    private final List<Question> questionsForTask;
    private View view;
    private String title;//String for tab title
    private String taskName;
    private static RecyclerView recyclerView;
    private UnansweredQuestionsAdapter adapter;

    public UnansweredQuestionsFragment(String title, final String taskName, List<Question> questionsForTask) {
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
        adapter = new UnansweredQuestionsAdapter(getActivity(), questionsForTask);
        recyclerView.setAdapter(adapter);
        return view;
    }
}
