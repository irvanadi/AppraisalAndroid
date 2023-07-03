package com.example.appraisalandroid.Page.Admin.Dashboard.Data;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.appraisalandroid.Adapter.PeriodAdapter;
import com.example.appraisalandroid.Models.Period;
import com.example.appraisalandroid.R;
import com.example.appraisalandroid.Utils.RecyclerViewInstance;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.divider.MaterialDividerItemDecoration;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class PeriodActivity extends AppCompatActivity {

    private RecyclerView rvPeriod;
    private DatabaseReference databaseReference;
    private ArrayList<Period> periods = new ArrayList<>();
    private TextInputLayout txtLayoutStartDate, txtLayoutEndDate;
    private TextInputEditText txtInputStartDate, txtInputEndDate, txtInputName;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
    private PeriodAdapter periodAdapter;
    private Long startSelection, endSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_period);

        getSupportActionBar().setTitle("Period");
        rvPeriod = findViewById(R.id.rvPeriod);
        databaseReference = FirebaseDatabase.getInstance().getReference();

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
        MaterialDividerItemDecoration materialDividerItemDecoration = new MaterialDividerItemDecoration(PeriodActivity.this, LinearLayoutManager.VERTICAL);
        materialDividerItemDecoration.setLastItemDecorated(false);
        materialDividerItemDecoration.setDividerInsetStart(3);
        RecyclerViewInstance.getRecyclerViewVertical(rvPeriod, this);
        periodAdapter = new PeriodAdapter(this, periods);
        rvPeriod.addItemDecoration(materialDividerItemDecoration);

        rvPeriod.setAdapter(periodAdapter);
        
    }

    private void loadData() {
        databaseReference.child("periods").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    periods.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Period period = dataSnapshot.getValue(Period.class);
                        periods.add(period);
                    }
                    periodAdapter.notifyDataSetChanged();
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
//        getMenuInflater().inflate(R.menu.add_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.add:
//                PopupMenu();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    private void PopupMenu(){
        final AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("Add new Period");

        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.pop_up_period, null);
        txtLayoutStartDate = view.findViewById(R.id.txtLayoutStartDate);
        txtInputStartDate = view.findViewById(R.id.txtEditStartDate);
        txtLayoutEndDate = view.findViewById(R.id.txtLayoutEndDate);
        txtInputEndDate = view.findViewById(R.id.txtEditEndDate);
        txtInputName = view.findViewById(R.id.txtEditName);
        txtInputStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateDialog();
            }
        });
        txtInputEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateDialog();
            }
        });
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();

        view.findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!txtInputStartDate.getText().equals("") || !txtInputEndDate.getText().equals("") || !txtInputName.getText().equals("")){
                    DatabaseReference query = databaseReference.child("periods").push();
                    query.child("name").setValue(txtInputName.getText().toString());
                    query.child("start").setValue(startSelection);
                    query.child("end").setValue(endSelection);
                }
                dialog.dismiss();
            }
        });
    }

    private void DateDialog() {
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        CalendarConstraints.Builder constraintsBuilderRange = new CalendarConstraints.Builder();
        constraintsBuilderRange.setValidator(DateValidatorPointForward.now());
        builder.setCalendarConstraints(constraintsBuilderRange.build());
        MaterialDatePicker<Pair<Long, Long>> materialDatePicker = builder.build();
        materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                txtInputStartDate.setText(simpleDateFormat.format(materialDatePicker.getSelection().first).toString());
                txtInputEndDate.setText(simpleDateFormat.format(materialDatePicker.getSelection().second).toString());
                startSelection = materialDatePicker.getSelection().first;
                endSelection = materialDatePicker.getSelection().second;
            }
        });
    }
}