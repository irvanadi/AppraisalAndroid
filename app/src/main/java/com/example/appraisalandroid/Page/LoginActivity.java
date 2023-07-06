package com.example.appraisalandroid.Page;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.appraisalandroid.Models.User;
import com.example.appraisalandroid.Page.Admin.MainActivity;
import com.example.appraisalandroid.Page.Employee.MainEmployeeActivity;
import com.example.appraisalandroid.R;
import com.example.appraisalandroid.Utils.ProgressDialogInstance;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference;
    private TextInputEditText etEmail, etPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etEmail = findViewById(R.id.textEditEmail);
        etPassword = findViewById(R.id.textEditPassword);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthCheck();
            }
        });
    }

    private void AuthCheck(){
        ProgressDialogInstance.createProgressDialoge(this, "Loading..");
        ProgressDialogInstance.showProgress();
        mFirebaseAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mFirebaseAuth.getCurrentUser();
                    // Sign in success, update UI with the signed-in user's information
                    mDatabaseReference.child("employees").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                                    if (dataSnapshot.child("email").getValue().toString().equals(user.getEmail())){
                                        if (dataSnapshot.child("role").getValue().toString().equals("3")){
                                            ProgressDialogInstance.dissmisProgress();
                                            SharedPreferences sharedPreferences = getSharedPreferences("employee_info", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("employee_id", dataSnapshot.getKey());
                                            editor.putString("role", dataSnapshot.child("role").getValue().toString());
                                            editor.putString("division", dataSnapshot.child("division_id").getValue().toString());
                                            editor.apply();
                                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                            finish();
                                        } else if (dataSnapshot.child("role").getValue().toString().equals("2")){
                                            ProgressDialogInstance.dissmisProgress();
                                            SharedPreferences sharedPreferences = getSharedPreferences("employee_info", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("employee_id", dataSnapshot.getKey());
                                            editor.putString("role", dataSnapshot.child("role").getValue().toString());
                                            editor.putString("division", dataSnapshot.child("division_id").getValue().toString());
                                            editor.apply();
                                            startActivity(new Intent(LoginActivity.this, MainEmployeeActivity.class));
                                            finish();
                                        } else if (dataSnapshot.child("role").getValue().toString().equals("1")){
                                            ProgressDialogInstance.dissmisProgress();
                                            SharedPreferences sharedPreferences = getSharedPreferences("employee_info", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("employee_id", dataSnapshot.getKey());
                                            editor.putString("role", dataSnapshot.child("role").getValue().toString());
                                            editor.putString("division", dataSnapshot.child("division_id").getValue().toString());
                                            editor.apply();
                                            startActivity(new Intent(LoginActivity.this, MainEmployeeActivity.class));
                                            finish();
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

                    Log.d("Login", "signInWithEmail:success");
                    Log.d("Login", "onComplete: "+ user);

                } else {
                    // If sign in fails, display a message to the user.
                    ProgressDialogInstance.dissmisProgress();
                    Log.w("Login", "signInWithEmail:failure", task.getException());
                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        ProgressDialogInstance.createProgressDialoge(LoginActivity.this, "Loading");
        ProgressDialogInstance.showProgress();
        if(mFirebaseUser != null){
            mDatabaseReference.child("employees").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                            if (dataSnapshot.child("email").getValue().toString().equals(mFirebaseUser.getEmail())){
                                if (dataSnapshot.child("role").getValue().toString().equals("3")){
                                    SharedPreferences sharedPreferences = getSharedPreferences("employee_info", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("employee_id", dataSnapshot.getKey());
                                    editor.putString("role", dataSnapshot.child("role").getValue().toString());
                                    editor.putString("division", dataSnapshot.child("division_id").getValue().toString());
                                    editor.apply();
                                    ProgressDialogInstance.dissmisProgress();
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                } else if (dataSnapshot.child("role").getValue().toString().equals("2")){
                                    SharedPreferences sharedPreferences = getSharedPreferences("employee_info", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("employee_id", dataSnapshot.getKey());
                                    editor.putString("role", dataSnapshot.child("role").getValue().toString());
                                    editor.putString("division", dataSnapshot.child("division_id").getValue().toString());
                                    editor.apply();
                                    ProgressDialogInstance.dissmisProgress();
                                    startActivity(new Intent(LoginActivity.this, MainEmployeeActivity.class));
                                    finish();
                                } else if (dataSnapshot.child("role").getValue().toString().equals("1")){
                                    SharedPreferences sharedPreferences = getSharedPreferences("employee_info", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("employee_id", dataSnapshot.getKey());
                                    editor.putString("role", dataSnapshot.child("role").getValue().toString());
                                    editor.putString("division", dataSnapshot.child("division_id").getValue().toString());
                                    editor.apply();
                                    ProgressDialogInstance.dissmisProgress();
                                    startActivity(new Intent(LoginActivity.this, MainEmployeeActivity.class));
                                    finish();
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
//            mDatabaseReference.child("employee").child(mFirebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    ProgressDialogInstance.dissmisProgress();
//                    User user = snapshot.getValue(User.class);
//                    if(user.role.equalsIgnoreCase("admin")){
//                        Log.d("User11", "onDataChange: " + user.role);
//                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                        finish();
//                    } else {
//                        Log.d("User12", "onDataChange: " + user.role);
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    Log.e("errUser", "onCancelled: ", error.toException());
//                }
//            });
        } else {
            ProgressDialogInstance.dissmisProgress();
        }
    }
}