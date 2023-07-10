package com.example.appraisalandroid.Page.Employee;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.appraisalandroid.Models.Answer;
import com.example.appraisalandroid.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private LineChart lcHistory;
    private DatabaseReference databaseReference;
    private ArrayList<Entry> yData;
    private SharedPreferences sharedPreferences;
    private String employee_id, employee_role, employee_division, generate_id;
    private double total, temp, percentage, divider;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        getActivity().setTitle("Home");

        lcHistory = view.findViewById(R.id.lcHistory);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        sharedPreferences = getActivity().getSharedPreferences("employee_info", MODE_PRIVATE);
        employee_id = sharedPreferences.getString("employee_id", "");
        employee_role = sharedPreferences.getString("role", "");
        employee_division = sharedPreferences.getString("division", "");
        loadData();

        return view;
    }

    private void loadData() {
        databaseReference.child("t_appraisal").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    total =0;
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        if (dataSnapshot.hasChild(employee_division)){
                            if (dataSnapshot.child(employee_division).child(employee_id).child("value").getValue() != null){
                                generate_id = dataSnapshot.getKey();
                                Log.d(TAG, "onDataChange1: " + generate_id);
                                for (DataSnapshot dataSnapshot1: dataSnapshot.child(employee_division).child(employee_id).child("value").getChildren()){
                                    Answer answer = dataSnapshot1.getValue(Answer.class);
                                    Log.d(TAG, "onDataChange2: " + answer.getValue());
                                    databaseReference.child("gen_values").child(generate_id).child("topics").orderByChild("topic_id").equalTo(answer.getTopic_id()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()){
                                                yData = new ArrayList<>();
                                                float i = 0;
                                                ArrayList<Double> values = new ArrayList<>();
                                                for (DataSnapshot dataSnapshot2: snapshot.getChildren()){
                                                    Log.d(TAG, "onDataChange: " + dataSnapshot2.getValue());
                                                    percentage = dataSnapshot2.child("value").getValue(Double.class);
                                                    temp = Double.parseDouble(answer.getValue());
                                                    divider = 5 * percentage;
                                                    total += ((temp * percentage) / divider) / dataSnapshot.child(employee_division).child(employee_id).child("value").getChildrenCount();
                                                    Log.d(TAG, "onDataChangeTemp: " + temp);
                                                    Log.d(TAG, "onDataChangeDivider: " + divider);
                                                    Log.d(TAG, "onDataChangeCount: " + dataSnapshot1.getChildrenCount());
                                                    Log.d(TAG, "onDataChangeTotal: " + total);
                                                    i++;
                                                    Float convVal = Float.parseFloat(String.valueOf(total));
                                                    yData.add(new Entry(i, convVal));

                                                    final LineDataSet lineDataSet = new LineDataSet(yData,"Period");
                                                    LineData lineData = new LineData(lineDataSet);
                                                    lcHistory.setData(lineData);
                                                    lcHistory.getAxisLeft().setDrawGridLines(false);
                                                    lcHistory.getXAxis().setDrawGridLines(false);
                                                    lcHistory.getDescription().setEnabled(false);
                                                    ValueFormatter valueFormatter = new ValueFormatter() {
                                                        @Override
                                                        public String getFormattedValue(float value) {
                                                            Log.d(TAG, "getFormattedValue1: " + value);
                                                            return super.getFormattedValue(value);
                                                        }

                                                        @Override
                                                        public String getAxisLabel(float value, AxisBase axis) {
                                                            Log.d(TAG, "getFormattedValue2: " + value + axis);
                                                            return super.getAxisLabel(value, axis);
                                                        }

                                                        @Override
                                                        public String getPointLabel(Entry entry) {
                                                            Log.d(TAG, "getFormattedValue3: " + entry);
                                                            return super.getPointLabel(entry);
                                                        }
                                                    };
                                                    XAxis xAxis = lcHistory.getXAxis();
                                                    xAxis.setValueFormatter(valueFormatter);
                                                    lcHistory.notifyDataSetChanged();
                                                    lcHistory.invalidate();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Log.e(TAG, "onCancelled: ", error.toException());
                                        }
                                    });
//                                    databaseReference.child("gen_values").child(generate_id).child("topics").orderByChild("topic_id").equalTo(answer.getTopic_id()).addValueEventListener(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                            if (snapshot.exists()){
//                                                yData = new ArrayList<>();
//                                                float i = 0;
//                                                double total = 0, temp, divider;
//                                                ArrayList<Double> values = new ArrayList<>();
//                                                for (DataSnapshot dataSnapshot2: snapshot.getChildren()){
//                                                    double percentage = dataSnapshot2.child("value").getValue(Double.class);
//                                                    double value = Double.parseDouble(answer.getValue());
//                                                    temp = value * percentage;
//                                                    divider = 5 * percentage;
//                                                    total += (temp / divider) / dataSnapshot1.getChildrenCount();
//                                                    values.add(total);
//                                                    Log.d(TAG, "onDataChange: " + dataSnapshot1.getChildrenCount());
//                                                    Log.d(TAG, "onDataChange1: " + dataSnapshot2.getValue().toString());
//                                                    Log.d(TAG, "onDataChangevalue: " + value);
//                                                    Log.d(TAG, "onDataChangepercent: " + percentage);
//                                                    Log.d(TAG, "onDataChangetotal: " + total);
//                                                    i++;
//                                                    Float convVal = Float.parseFloat(String.valueOf(total));
//                                                    yData.add(new Entry(i, convVal));
//
//                                                    final LineDataSet lineDataSet = new LineDataSet(yData,"Period");
//                                                    LineData lineData = new LineData(lineDataSet);
//                                                    lcHistory.setData(lineData);
//                                                    lcHistory.notifyDataSetChanged();
//                                                    lcHistory.invalidate();
//                                                }
////                                                Log.d(TAG, "onDataChange: " + total);
////                                                Toast.makeText(getContext(), String.valueOf(total), Toast.LENGTH_SHORT).show();
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError error) {
//                                            Log.e(TAG, "onCancelled: ", error.toException());
//                                        }
//                                    });
                                }
                            }
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