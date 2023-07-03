package com.example.appraisalandroid.Page.Admin.Dashboard.Data.Employee;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.appraisalandroid.Models.Division;
import com.example.appraisalandroid.Models.Employee;
import com.example.appraisalandroid.Page.Admin.Dashboard.Data.EmployeeActivity;
import com.example.appraisalandroid.R;
import com.example.appraisalandroid.Utils.ProgressDialogInstance;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddEmployeeActivity extends AppCompatActivity{

    private TextInputEditText txtName, txtEmail, txtPhone, txtPassword;
    private Spinner spnDivision, spnRole;
    private Button btnSave;
    private DatabaseReference databaseReference;
    private ArrayList<Division> divisions = new ArrayList<>();
    private ArrayList<String> results = new ArrayList<>();
    private ArrayList<String> roles = new ArrayList<>();
    private String selectedValue, selectedRole;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);
        getSupportActionBar().setTitle("Add Employee");
        txtName = findViewById(R.id.txtEditName);
        txtEmail = findViewById(R.id.txtEditEmail);
        txtPassword = findViewById(R.id.txtEditPassword);
        txtPhone = findViewById(R.id.txtEditPhone);
        spnDivision = findViewById(R.id.spnDivision);
        btnSave = findViewById(R.id.btnSave);
        mFirebaseAuth = FirebaseAuth.getInstance();
        spnRole = findViewById(R.id.spnRole);
        roles.add("Employee");
        roles.add("Leader");
        roles.add("Admin");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        spnDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedValue = divisions.get(i).getKey();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spnRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedRole = String.valueOf(i + 1);
                Log.d(TAG, "onItemSelected: " + selectedRole);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });

        loadSpinner();
//        loadFragment(new AddEmployee1Fragment());
    }

//    private boolean loadFragment(Fragment fragment) {
//        // load fragment
//        if (fragment != null){
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.frameLayout, fragment)
//                    .commit();
//            return true;
//        }
//        return false;
//    }

    private void loadSpinner() {
        databaseReference.child("divisions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                divisions.clear();
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Division division = dataSnapshot.getValue(Division.class);
                        division.setKey(dataSnapshot.getKey());
//                        Toast.makeText(AddEmployeeActivity.this, division.key, Toast.LENGTH_SHORT).show();

                        divisions.add(division);
                        results.add(division.name);
                    }
                    if (divisions.size() <= 0){
                        Toast.makeText(AddEmployeeActivity.this, "Please Input at least 1 Division", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddEmployeeActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, results);
                spnDivision.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: ", error.toException());
            }
        });
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(AddEmployeeActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, roles);
        spnRole.setAdapter(stringArrayAdapter);
    }

    private void saveData() {
        ProgressDialogInstance.createProgressDialoge(this, "Process Input Data");
        ProgressDialogInstance.showProgress();
//        Toast.makeText(this, selectedValue, Toast.LENGTH_SHORT).show();
        Employee employee = new Employee(txtName.getText().toString(), "62" + txtPhone.getText().toString(), txtEmail.getText().toString(), txtPassword.getText().toString(), selectedValue, selectedRole.toLowerCase());
        databaseReference.child("employees").push().setValue(employee).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    mFirebaseAuth.createUserWithEmailAndPassword(txtEmail.getText().toString(), txtPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                ProgressDialogInstance.dissmisProgress();
                                Toast.makeText(AddEmployeeActivity.this, "Employee has been added", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddEmployeeActivity.this, EmployeeActivity.class);
                                intent.putExtra("addEmployee", "success");
                                finish();
                            } else {
                                Log.e(TAG, "onComplete: ", task.getException());
                            }
                        }
                    });
//                    Snackbar snackbar = Snackbar.make(findViewById(R.id.constraintAddEmployee), "Employee has been added", Snackbar.LENGTH_SHORT);
//                    snackbar.show();
//                    finish();
                } else {
                    Log.e(TAG, "onComplete: ", task.getException());
                }
            }
        });
    }

//    @Override
//    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//        Log.d(TAG, "onItemSelected1: " + results.get(position));
//        Log.d(TAG, "onItemSelected2: " + divisions.get(position));
//        selectedValue = divisions.get(position).getKey();
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> adapterView) {
//    }

}