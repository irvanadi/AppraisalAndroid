package com.example.appraisalandroid.Page.Employee;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
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
import com.example.appraisalandroid.Adapter.DivisionAdapter;
import com.example.appraisalandroid.Models.Employee;
import com.example.appraisalandroid.Models.GenValue;
import com.example.appraisalandroid.Models.GenerateValue;
import com.example.appraisalandroid.Models.Rater;
import com.example.appraisalandroid.R;
import com.example.appraisalandroid.Utils.RecyclerViewInstance;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.divider.MaterialDividerItemDecoration;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AppraisalFragment#} factory method to
 * create an instance of this fragment.
 */
public class AppraisalFragment extends Fragment {

    private DatabaseReference databaseReference;
    private FirebaseAuth mFirebaseAuth;
    private String employeeKey, divisionKey, generateKey = "";
    private ArrayList<String> performance_employee = new ArrayList<>();
    private RecyclerView rvAppraisal;
    private ArrayList<Employee> employees = new ArrayList<>();
    private ArrayList<GenValue> genValues = new ArrayList<>();
    private ArrayList<GenerateValue> generateValues = new ArrayList<>();
    private AppraisalAdapter appraisalAdapter;
    public AppraisalFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_appraisal, container, false);
        getActivity().setTitle("Appraisal");

        mFirebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        rvAppraisal = view.findViewById(R.id.rvAppraisal);

        initRecyclerView();
        loadData();
        return view;
    }

    private void initRecyclerView() {
        MaterialDividerItemDecoration materialDividerItemDecoration = new MaterialDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        materialDividerItemDecoration.setLastItemDecorated(false);
        materialDividerItemDecoration.setDividerInsetStart(3);
        RecyclerViewInstance.getRecyclerViewVertical(rvAppraisal, getContext());
        rvAppraisal.addItemDecoration(materialDividerItemDecoration);
        appraisalAdapter = new AppraisalAdapter(getContext(), employees);
        rvAppraisal.setAdapter(appraisalAdapter);
    }

    private void loadData() {
        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        databaseReference.child("employees").orderByChild("email").equalTo(mFirebaseUser.getEmail()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Log.d(TAG, "onDataChange: " + dataSnapshot.getKey());
                        Employee employee = dataSnapshot.getValue(Employee.class);
                        employeeKey = dataSnapshot.getKey();
                        divisionKey = employee.getDivision_id();
                        databaseReference.child("gen_values").orderByChild("status").equalTo("active").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    for (DataSnapshot dataSnapshot1: snapshot.getChildren()){
                                        GenValue genValue = new GenValue();
                                        genValue.setKey(dataSnapshot1.getKey());
                                        genValue.setConsistency(dataSnapshot1.child("consistency").getValue(Double.class));
                                        genValue.setPeriod_id(dataSnapshot1.child("period_id").getValue().toString());
                                        generateKey = dataSnapshot1.getKey();
                                        databaseReference.child("t_appraisal").child(generateKey).child(divisionKey).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                                if (snapshot1.exists()){
//                                                    if (snapshot1.child("generate_id").getValue() != null) generateKey = snapshot1.child("generate_id").getValue().toString();
                                                    Log.d(TAG, "onDataChangekey: " + generateKey);
                                                    for (DataSnapshot dataSnapshot1 : snapshot1.getChildren()){
                                                        if (!dataSnapshot1.getKey().equals(employeeKey)){
                                                            Log.d(TAG, "onDataChange1: " + dataSnapshot1.getKey());
                                                            Log.d(TAG, "onDataChange2: " + dataSnapshot1.child("rater").getValue());
                                                            employees.clear();
                                                            for (DataSnapshot dataSnapshot2 : dataSnapshot1.child("rater").getChildren()){
                                                                if (dataSnapshot2.getValue(Rater.class).getRater_id().equals(employeeKey)){
                                                                    Log.d(TAG, "onDataChange3: " + dataSnapshot2.getValue());
                                                                    Log.d(TAG, "onDataChange4Perform: " + dataSnapshot1.getKey());
                                                                    databaseReference.child("employees").child(dataSnapshot1.getKey()).addValueEventListener(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot snapshot3) {
                                                                            if (snapshot3.exists()){
                                                                                Employee employeePerform = snapshot3.getValue(Employee.class);
                                                                                Log.d(TAG, "onDataChange23: " + employee.getRole());
                                                                                employeePerform.setGenerate_id(generateKey);
                                                                                employeePerform.setKey(snapshot3.getKey());
                                                                                employees.add(employeePerform);
//                                                                if (employees.size() >= 2){
//                                                                    Collections.sort(employees, );
//                                                                }
                                                                                appraisalAdapter.notifyDataSetChanged();
                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError error) {
                                                                            Log.e(TAG, "onCancelled: ", error.toException());
                                                                        }
                                                                    });
//                                                    performance_employee.add(dataSnapshot1.getKey());
                                                                }
                                                            }
                                                            saveEmployeeId();
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


                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e(TAG, "onCancelled: ", error.toException());
                            }
                        });
//                        databaseReference.child("t_appraisal").child(divisionKey).addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot1) {
//                                if (snapshot1.exists()){
//                                    if (snapshot1.child("generate_id").getValue() != null) generateKey = snapshot1.child("generate_id").getValue().toString();
//                                    Log.d(TAG, "onDataChangekey: " + generateKey);
//                                    for (DataSnapshot dataSnapshot1 : snapshot1.getChildren()){
//                                        if (!dataSnapshot1.getKey().equals(employeeKey)){
//                                            Log.d(TAG, "onDataChange1: " + dataSnapshot1.getKey());
//                                            Log.d(TAG, "onDataChange2: " + dataSnapshot1.child("rater").getValue());
//                                            employees.clear();
//                                            for (DataSnapshot dataSnapshot2 : dataSnapshot1.child("rater").getChildren()){
//                                                if (dataSnapshot2.getValue(Rater.class).getRater_id().equals(employeeKey)){
//                                                    Log.d(TAG, "onDataChange3: " + dataSnapshot2.getValue());
//                                                    Log.d(TAG, "onDataChange4Perform: " + dataSnapshot1.getKey());
//                                                    databaseReference.child("employees").child(dataSnapshot1.getKey()).addValueEventListener(new ValueEventListener() {
//                                                        @Override
//                                                        public void onDataChange(@NonNull DataSnapshot snapshot3) {
//                                                            if (snapshot3.exists()){
//                                                                Employee employeePerform = snapshot3.getValue(Employee.class);
//                                                                Log.d(TAG, "onDataChange23: " + employee.getRole());
//                                                                employeePerform.setGenerate_id(generateKey);
//                                                                employeePerform.setKey(snapshot3.getKey());
//                                                                employees.add(employeePerform);
////                                                                if (employees.size() >= 2){
////                                                                    Collections.sort(employees, );
////                                                                }
//                                                                appraisalAdapter.notifyDataSetChanged();
//                                                            }
//                                                        }
//
//                                                        @Override
//                                                        public void onCancelled(@NonNull DatabaseError error) {
//                                                            Log.e(TAG, "onCancelled: ", error.toException());
//                                                        }
//                                                    });
////                                                    performance_employee.add(dataSnapshot1.getKey());
//                                                }
//                                            }
//                                            saveEmployeeId();
//                                        }
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//                                Log.e(TAG, "onCancelled: ", error.toException());
//                            }
//                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: ", error.toException());
            }
        });
    }

    private void saveEmployeeId() {
//        Log.d(TAG, "saveEmployeeIdsz: " + performance_employee.size());
//        for (int i = 0; i < performance_employee.size(); i++) {
//            Log.d(TAG, "saveEmployeeId: " + performance_employee.get(i).toString());
//        }
//        Log.d(TAG, "saveEmployeeIdgenKey: " +generateKey);

    }
}