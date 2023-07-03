package com.example.appraisalandroid.Page.Admin.Dashboard.Data;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.appraisalandroid.Adapter.EmployeeAdapter;
import com.example.appraisalandroid.Models.Division;
import com.example.appraisalandroid.Models.Employee;
import com.example.appraisalandroid.Page.Admin.Dashboard.Data.Employee.AddEmployeeActivity;
import com.example.appraisalandroid.R;
import com.example.appraisalandroid.Utils.RecyclerViewInstance;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.divider.MaterialDividerItemDecoration;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EmployeeActivity extends AppCompatActivity {

    private RecyclerView rvEmployee;
    private DatabaseReference databaseReference;
    private ArrayList<Employee> employees = new ArrayList<>();
    private TextInputEditText txtInputDivision;
    private EmployeeAdapter employeeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
        rvEmployee = findViewById(R.id.rvEmployee);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        getSupportActionBar().setTitle("Employee");

        findViewById(R.id.fabAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EmployeeActivity.this, AddEmployeeActivity.class));
            }
        });

        initRecyclerView();
        loadData();
    }

    private void initRecyclerView() {
        MaterialDividerItemDecoration materialDividerItemDecoration = new MaterialDividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        materialDividerItemDecoration.setLastItemDecorated(false);
        materialDividerItemDecoration.setDividerInsetStart(3);
        RecyclerViewInstance.getRecyclerViewVertical(rvEmployee, this);
        rvEmployee.addItemDecoration(materialDividerItemDecoration);
        employeeAdapter = new EmployeeAdapter(this, employees);
        rvEmployee.setAdapter(employeeAdapter);
    }

    private void loadData() {
        databaseReference.child("employees").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    employees.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Employee employee = dataSnapshot.getValue(Employee.class);
                        if (employee.division_id != null){
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("divisions").child(employee.division_id);
                            reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        Division division = task.getResult().getValue(Division.class);
                                        employee.setDivision(division);
                                        employees.add(employee);
                                        Log.d(TAG, "onComplete: " + employee.division_id + " " + employee.division.getName());
                                        employeeAdapter.notifyDataSetChanged();
                                    }
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
//        getMenuInflater().inflate(R.menu.add_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.add:
//                startActivity(new Intent(this, AddEmployeeActivity.class));
////                PopupMenu();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }
}