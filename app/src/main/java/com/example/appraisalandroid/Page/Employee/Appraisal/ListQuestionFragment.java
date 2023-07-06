package com.example.appraisalandroid.Page.Employee.Appraisal;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appraisalandroid.Adapter.AppraisalAdapter;
import com.example.appraisalandroid.Adapter.ListQuestionAdapter;
import com.example.appraisalandroid.Models.Assessment;
import com.example.appraisalandroid.Models.Topic;
import com.example.appraisalandroid.R;
import com.example.appraisalandroid.Utils.RecyclerViewInstance;
import com.google.android.material.divider.MaterialDividerItemDecoration;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListQuestionFragment} factory method to
 * create an instance of this fragment.
 */
public class ListQuestionFragment extends Fragment {

    private RecyclerView rvAssesment;
    private DatabaseReference databaseReference;
    private String generate_id = "", performance_id = "";
    private ArrayList<Topic> topics = new ArrayList<>();
    private ArrayList<Assessment> assessments = new ArrayList<>();
    private ListQuestionAdapter listQuestionAdapter;

    public ListQuestionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_question, container, false);
        rvAssesment = view.findViewById(R.id.rvAssesment);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        generate_id = getActivity().getIntent().getStringExtra("generate_id");
        performance_id = getActivity().getIntent().getStringExtra("performance_id");

        initRecyclerView();
        loadData();

        return view;
    }

    private void initRecyclerView() {
//        MaterialDividerItemDecoration materialDividerItemDecoration = new MaterialDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
//        materialDividerItemDecoration.setLastItemDecorated(false);
//        materialDividerItemDecoration.setDividerInsetStart(3);
        RecyclerViewInstance.getRecyclerViewVertical(rvAssesment, getContext());
//        rvAssesment.addItemDecoration(materialDividerItemDecoration);
        listQuestionAdapter = new ListQuestionAdapter(getContext(), topics);
        rvAssesment.setAdapter(listQuestionAdapter);
    }

    private void loadData() {
        databaseReference.child("gen_values").child(generate_id).child("topics").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    topics.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Topic topic = new Topic();
                        topic.setKey(dataSnapshot.child("topic_id").getValue().toString());
                        topic.setName(dataSnapshot.child("topic_name").getValue().toString());
                        Log.d(TAG, "onDataChange22: " + topic.getName());
                        Log.d(TAG, "onDataChange22Key: " + topic.getKey());
                        databaseReference.child("assessments").child(topic.getKey()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                if (snapshot1.exists()){
                                    assessments.clear();
                                    for(DataSnapshot dataSnapshot1: snapshot1.getChildren()){
                                        Assessment assessment = dataSnapshot1.getValue(Assessment.class);
                                        assessment.setKey(dataSnapshot1.getKey());
                                        Log.d(TAG, "onDataChange223: " + assessment.getName());
                                        assessments.add(assessment);
                                    }
                                    topic.setPerformance_id(performance_id);
                                    topic.setAssessments(assessments);
                                    topics.add(topic);
                                } else {
                                    topics.add(topic);
                                }
                                listQuestionAdapter.notifyDataSetChanged();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e(TAG, "onCancelled: ", error.toException());
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: ", error.toException());
            }
        });
    }
}