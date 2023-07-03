package com.example.appraisalandroid.Page.Admin.Dashboard.Data;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.appraisalandroid.Adapter.GenerateAdapter;
import com.example.appraisalandroid.Models.GenValue;
import com.example.appraisalandroid.Models.GenerateValue;
import com.example.appraisalandroid.Models.Period;
import com.example.appraisalandroid.Page.Admin.Dashboard.Data.Generate.GenerateProcessActivity;
import com.example.appraisalandroid.R;
import com.example.appraisalandroid.Utils.RecyclerViewInstance;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.divider.MaterialDividerItemDecoration;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GenerateActivity extends AppCompatActivity {

    private RecyclerView rvGenerate;
    private DatabaseReference databaseReference;
    private ArrayList<GenValue> genValues = new ArrayList<>();
    private ArrayList<GenerateValue> generateValues = new ArrayList<>();
    private GenerateAdapter generateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);

        rvGenerate = findViewById(R.id.rvGenerate);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        getSupportActionBar().setTitle("Generate Topic Value");

        findViewById(R.id.fabAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GenerateActivity.this, GenerateProcessActivity.class));
            }
        });

        String getData = getIntent().getStringExtra("generate");
        if (getData != null && getData.equals("success")){
            genValues.clear();
        }

        initRecyclerView();
        loadData3();
    }

    private void initRecyclerView() {
        MaterialDividerItemDecoration materialDividerItemDecoration = new MaterialDividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        materialDividerItemDecoration.setLastItemDecorated(false);
        materialDividerItemDecoration.setDividerInsetStart(3);
        RecyclerViewInstance.getRecyclerViewVertical(rvGenerate, this);
        rvGenerate.addItemDecoration(materialDividerItemDecoration);
        generateAdapter = new GenerateAdapter(this, genValues);
        rvGenerate.setAdapter(generateAdapter);
    }

    private void loadData3(){
        databaseReference.child("gen_values").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    genValues.clear();
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        GenValue genValue = dataSnapshot.getValue(GenValue.class);
                        genValue.setKey(dataSnapshot.getKey());
                        Log.d(TAG, "onDataChange: " + genValue.getKey());
                        if (genValue.getPeriod_id() != null){
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("periods").child(genValue.period_id);
                            reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (task.isSuccessful()){
                                        Period period = task.getResult().getValue(Period.class);
                                        genValue.setPeriod(period);
                                        genValues.add(genValue);
                                        Log.d(TAG, "onComplete: " + genValues.size());
                                    }
                                    generateAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                        Log.d(TAG, "onDataChange: " + dataSnapshot.getValue().toString());
                        Log.d(TAG, "onDataChange: " + genValue.getConsistency());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: ",error.toException() );
            }
        });
    }

    private void loadData2() {
        databaseReference.child("gen_values").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    genValues.clear();
                    generateValues.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("periods").child(dataSnapshot.child("period_id").getValue().toString());
                        databaseReference1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                if (snapshot2.exists()){
                                    Period period = snapshot2.getValue(Period.class);
                                    for (DataSnapshot dataSnapshot1: dataSnapshot.child("topics").getChildren()){
                                        GenerateValue generateValue = dataSnapshot1.getValue(GenerateValue.class);
                                        generateValues.add(generateValue);
                                    }
                                    GenValue genValue = new GenValue(generateValues,Double.parseDouble(dataSnapshot.child("consistency").getValue().toString()), period);
                                    genValues.add(genValue);
                                    generateAdapter.notifyDataSetChanged();
                                }
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
    private void loadData() {
        databaseReference.child("gen_values").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    genValues.clear();
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        if (dataSnapshot.exists()){
                            generateValues.clear();
                            for (DataSnapshot dataSnapshot2 : dataSnapshot.child("topics").getChildren()){
                                GenerateValue generateValue = dataSnapshot2.getValue(GenerateValue.class);
                                generateValues.add(generateValue);
                                Log.d(TAG, "onDataChange2: " + generateValue.getTopic_name());
                            }
                            databaseReference.child("periods").child(dataSnapshot.child("period_id").getValue().toString()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        Period period = snapshot.getValue(Period.class);
                                        Log.d(TAG, "onDataChange23: " + generateValues.get(0).getValue());
                                        generateAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.e(TAG, "onCancelled: ", error.toException());
                                }
                            });
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: ", error.toException());
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.generate_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.generate:
//                startActivity(new Intent(this, GenerateProcessActivity.class));
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
}