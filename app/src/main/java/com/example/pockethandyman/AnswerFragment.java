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
    private final ArrayList<String> questionsForTask;
    private View view;
    private String title;//String for tab title
    private String taskName;
    private static RecyclerView recyclerView;
    private RecyclerView_Adapter adapter;
//    private DatabaseReference dbRef;
//    private ArrayList<String> questionsForTask = new ArrayList<>();

    public AnswerFragment(String title, final String taskName, ArrayList<String> questionsForTask) {
        this.title = title; // Setting tab title
        this.taskName = taskName;
        this.questionsForTask = questionsForTask;

//        dbRef = FirebaseDatabase.getInstance().getReference().child("questions");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.button_activity_fragment, container, false);

        setRecyclerView();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

//        adapter = new RecyclerView_Adapter(getActivity(), questionsForTask);
//        recyclerView.setAdapter(adapter);

        return view;
    }


    private void setRecyclerView() {
//        final ArrayList<String> questionsForTask = new ArrayList<>();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

//        ArrayList<String> arrayList = new ArrayList<>();
//        for (int i = 0; i < 3; i++) {
//            arrayList.add(title+" Items " + i);
//        }

//        final RecyclerView_Adapter adapter = new RecyclerView_Adapter(getActivity(), questionsForTask);
//        recyclerView.setAdapter(adapter);

//        dbRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String question = "",
//                       category = "";
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    DataSnapshot questionSnapshot = ds.child("question");
//                    question = (String)questionSnapshot.getValue();
//
//                    DataSnapshot categorySnapshot = ds.child("category");
//                    category = (String)categorySnapshot.getValue();
//
//                    if (category.equals(taskName)) {
//                        questionsForTask.add(question);
//                    }
//                }
//
//                Log.d(TAG, "question: " + question);
//
//                adapter = new RecyclerView_Adapter(getActivity(), questionsForTask);
//                recyclerView.setAdapter(adapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        adapter = new RecyclerView_Adapter(getActivity(), questionsForTask);
        recyclerView.setAdapter(adapter);
    }
}
