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

public class AnswerFragment extends Fragment {
    private static final String TAG = "AnswerFragment";
    private final ArrayList<Question> questionsForTask;
    private View view;
    private String title;//String for tab title
    private String taskName;
    private static RecyclerView recyclerView;
    private RecyclerView_Adapter adapter;

    public AnswerFragment(String title, final String taskName, ArrayList<Question> questionsForTask) {
        this.title = title; // Setting tab title
        this.taskName = taskName;
        this.questionsForTask = questionsForTask;
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

        adapter = new RecyclerView_Adapter(getActivity(), questionsForTask, taskName);
        recyclerView.setAdapter(adapter);
    }
}
