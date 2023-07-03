package com.example.appraisalandroid.Page.Admin.Dashboard.Data.Topic;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.appraisalandroid.Adapter.AssesmentAdapter;
import com.example.appraisalandroid.Adapter.DivisionAdapter;
import com.example.appraisalandroid.Models.Assessment;
import com.example.appraisalandroid.R;
import com.example.appraisalandroid.Utils.RecyclerViewInstance;
import com.google.android.material.divider.MaterialDividerItemDecoration;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AssessmentActivity extends AppCompatActivity {

    private RecyclerView rvAssesment;
    private DatabaseReference databaseReference;
    private String topic, key;
    private TextInputEditText txtInputAssesment;
    private ArrayList<Assessment> assessments = new ArrayList<>();
    private AssesmentAdapter assesmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);
        rvAssesment = findViewById(R.id.rvAssesment);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        topic = getIntent().getStringExtra("topic");
        key = getIntent().getStringExtra("key");
        getSupportActionBar().setTitle("Assesment of " + topic);
        Log.d(TAG, "onCreate: " + key);

        findViewById(R.id.fabAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu();
            }
        });

        initRecyclerView();
        loadData();
    }

    private void initRecyclerView() {
        MaterialDividerItemDecoration materialDividerItemDecoration = new MaterialDividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        materialDividerItemDecoration.setLastItemDecorated(false);
        materialDividerItemDecoration.setDividerInsetStart(3);
        RecyclerViewInstance.getRecyclerViewVertical(rvAssesment, this);
        rvAssesment.addItemDecoration(materialDividerItemDecoration);
        assesmentAdapter = new AssesmentAdapter(this, assessments);
        rvAssesment.setAdapter(assesmentAdapter);
    }

    private void loadData() {
        databaseReference.child("assessments").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    assessments.clear();
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        Assessment assessment = dataSnapshot.getValue(Assessment.class);
                        assessment.setKey(dataSnapshot.getKey());
                        assessments.add(assessment);
                    }
                    assesmentAdapter.notifyDataSetChanged();
                    Log.d(TAG, "onDataChange: " + String.valueOf(snapshot.getValue()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: ", error.toException());
            }
        });
    }

    private void PopupMenu(){
        final AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("Add New Assessment");

        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.pop_up_assessment, null);
        txtInputAssesment = view.findViewById(R.id.txtEditAssesment);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();

        view.findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Assessment assessment = new Assessment(txtInputAssesment.getText().toString(), key);
                databaseReference.child("assessments").child(key).push().setValue(assessment);
                dialog.dismiss();
            }
        });
    }

}