package com.example.appraisalandroid.Page.Employee.Appraisal;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.appraisalandroid.Models.Answer;
import com.example.appraisalandroid.Models.Assessment;
import com.example.appraisalandroid.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

public class QuestionnaireActivity extends AppCompatActivity {

    private CardView cvAnswer1, cvAnswer2, cvAnswer3, cvAnswer4, cvAnswer5;
    private ExtendedFloatingActionButton fabNext, fabFinish;
    private ArrayList<String> questions, questionsKey;
    private ArrayList<Assessment> assessments;
    private TextView txtAnswer1, txtAnswer2, txtAnswer3, txtAnswer4, txtAnswer5, txtQuestion;
    private int count = 0;
    private String answer, performance_id = "", employee_id, employee_role, employee_division, topic_id;
    private DatabaseReference databaseReference;
    private ArrayList<Answer> answers = new ArrayList<>();
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        cvAnswer1 = findViewById(R.id.cvAnswer1);
        cvAnswer2 = findViewById(R.id.cvAnswer2);
        cvAnswer3 = findViewById(R.id.cvAnswer3);
        cvAnswer4 = findViewById(R.id.cvAnswer4);
        cvAnswer5 = findViewById(R.id.cvAnswer5);
        txtQuestion = findViewById(R.id.txtQuestion);
        fabNext = findViewById(R.id.fabNext);
        assessments = (ArrayList<Assessment>) getIntent().getSerializableExtra("assessmentsList");
        performance_id = getIntent().getStringExtra("performance_id");
        questions = getIntent().getStringArrayListExtra("assessments");
        questionsKey = getIntent().getStringArrayListExtra("assessmentsKey");
        topic_id = getIntent().getStringExtra("topic_id");
        sharedPreferences = getSharedPreferences("employee_info", MODE_PRIVATE);
        fabFinish = findViewById(R.id.fabFinish);

        txtQuestion.setText(questions.get(count));
        Log.d(TAG, "onCreate: " + questions.get(count));

        Log.d(TAG, "onCreate: " + questions.size());


        cvAnswer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cvAnswer1.setCardBackgroundColor(ContextCompat.getColor(QuestionnaireActivity.this, R.color.teal_200));
                cvAnswer2.setCardBackgroundColor(ContextCompat.getColor(QuestionnaireActivity.this, R.color.white));
                cvAnswer3.setCardBackgroundColor(ContextCompat.getColor(QuestionnaireActivity.this, R.color.white));
                cvAnswer4.setCardBackgroundColor(ContextCompat.getColor(QuestionnaireActivity.this, R.color.white));
                cvAnswer5.setCardBackgroundColor(ContextCompat.getColor(QuestionnaireActivity.this, R.color.white));
                answer = "5";
            }
        });

        cvAnswer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cvAnswer1.setCardBackgroundColor(ContextCompat.getColor(QuestionnaireActivity.this, R.color.white));
                cvAnswer2.setCardBackgroundColor(ContextCompat.getColor(QuestionnaireActivity.this, R.color.teal_200));
                cvAnswer3.setCardBackgroundColor(ContextCompat.getColor(QuestionnaireActivity.this, R.color.white));
                cvAnswer4.setCardBackgroundColor(ContextCompat.getColor(QuestionnaireActivity.this, R.color.white));
                cvAnswer5.setCardBackgroundColor(ContextCompat.getColor(QuestionnaireActivity.this, R.color.white));
                answer = "4";
            }
        });

        cvAnswer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cvAnswer1.setCardBackgroundColor(ContextCompat.getColor(QuestionnaireActivity.this, R.color.white));
                cvAnswer2.setCardBackgroundColor(ContextCompat.getColor(QuestionnaireActivity.this, R.color.white));
                cvAnswer3.setCardBackgroundColor(ContextCompat.getColor(QuestionnaireActivity.this, R.color.teal_200));
                cvAnswer4.setCardBackgroundColor(ContextCompat.getColor(QuestionnaireActivity.this, R.color.white));
                cvAnswer5.setCardBackgroundColor(ContextCompat.getColor(QuestionnaireActivity.this, R.color.white));
                answer = "3";
            }
        });

        cvAnswer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cvAnswer1.setCardBackgroundColor(ContextCompat.getColor(QuestionnaireActivity.this, R.color.white));
                cvAnswer2.setCardBackgroundColor(ContextCompat.getColor(QuestionnaireActivity.this, R.color.white));
                cvAnswer3.setCardBackgroundColor(ContextCompat.getColor(QuestionnaireActivity.this, R.color.white));
                cvAnswer4.setCardBackgroundColor(ContextCompat.getColor(QuestionnaireActivity.this, R.color.teal_200));
                cvAnswer5.setCardBackgroundColor(ContextCompat.getColor(QuestionnaireActivity.this, R.color.white));
                answer = "2";
            }
        });

        cvAnswer5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cvAnswer1.setCardBackgroundColor(ContextCompat.getColor(QuestionnaireActivity.this, R.color.white));
                cvAnswer2.setCardBackgroundColor(ContextCompat.getColor(QuestionnaireActivity.this, R.color.white));
                cvAnswer3.setCardBackgroundColor(ContextCompat.getColor(QuestionnaireActivity.this, R.color.white));
                cvAnswer4.setCardBackgroundColor(ContextCompat.getColor(QuestionnaireActivity.this, R.color.white));
                cvAnswer5.setCardBackgroundColor(ContextCompat.getColor(QuestionnaireActivity.this, R.color.teal_200));
                answer = "1";
            }
        });

        fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count < questions.size() - 1){
                    saveAnswer();
                } else {
                    saveAndVisible();
                }
            }
        });

        fabFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClicke: " + "asdasd");
                saveAndExit();
            }
        });
    }

    private void saveAndExit() {
        employee_id = sharedPreferences.getString("employee_id", "");
        employee_role = sharedPreferences.getString("role", "");
        employee_division = sharedPreferences.getString("division", "");
        Answer answer2 = new Answer();
        answer2.setEmployee_id(employee_id);
        answer2.setAssessment_id(questionsKey.get(count));
        answer2.setTopic_id(topic_id);
        answer2.setValue(answer);
        Log.d(TAG, "saveAndExit: " + answer2.getValue());
        Log.d(TAG, "saveAndExit: " + answer2.getAssessment_id());
        Log.d(TAG, "saveAndExit: " + answer2.getEmployee_id());
        Log.d(TAG, "saveAndExit: " + answer2.getTopic_id());
        Log.d(TAG, "saveAndExit: " + employee_role);
        Log.d(TAG, "saveAndExit: " + employee_division);


        if (!employee_id.equals("") && !employee_role.equals("") && !employee_division.equals("") &&
                answer2.getAssessment_id() != null && answer2.getTopic_id() != null && answer2.getValue() != null){
            databaseReference.child("t_appraisal").child(employee_division).child(performance_id).child("value").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        answers.clear();
                        for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                            Answer answerSnapshot = dataSnapshot.getValue(Answer.class);
                            answerSnapshot.setKey(dataSnapshot.getKey());
                            answers.add(answerSnapshot);
                        }
                        String key = null;
                        if (answers.stream().anyMatch(answer1 -> answer1.getAssessment_id().equals(answer2.getAssessment_id()) &&
                                answer1.getEmployee_id().equals(answer2.getEmployee_id()) && answer1.getTopic_id().equals(answer2.getTopic_id()))){
                            key = answers.stream().filter(answer1 -> answer1.getAssessment_id().equals(answer2.getAssessment_id()) &&
                                            answer1.getEmployee_id().equals(answer2.getEmployee_id()) && answer1.getTopic_id().equals(answer2.getTopic_id()))
                                    .findFirst().get().getKey();
                        }
//                        if (answers.stream().anyMatch(answer1 -> answer1.getAssessment_id().equals(answer2.getAssessment_id()) &&
//                                        answer1.getEmployee_id().equals(answer2.getEmployee_id()) && answer1.getTopic_id().equals(answer2.getTopic_id()))){
//                            key = answers.stream().filter(answer1 -> answer1.getAssessment_id().equals(answer2.getAssessment_id()) &&
//                                            answer1.getEmployee_id().equals(answer2.getEmployee_id()) && answer1.getTopic_id().equals(answer2.getTopic_id()))
//                                    .findFirst().get().getKey();
//                        }
                        if (key != null){
                            databaseReference.child("t_appraisal").child(employee_division).child(performance_id).child("value").child(key).setValue(answer2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        finish();
//                                        clearAll();
                                    }
                                }
                            });
                        } else {
                            Log.d(TAG, "saveAndExit: key not exist");
                            databaseReference.child("t_appraisal").child(employee_division).child(performance_id).child("value").push().setValue(answer2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        finish();
//                                    clearAll();
                                    }
                                }
                            });
                        }
                    } else {
                        Log.d(TAG, "saveAndExit: snapshot not exist");
                        databaseReference.child("t_appraisal").child(employee_division).child(performance_id).child("value").push().setValue(answer2).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    finish();
//                                    clearAll();
                                }
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "onCancelled: ", error.toException());
                }
            });
//            databaseReference.child("t_appraisal").child(employee_division).child(performance_id).child("value").push().setValue(answer2).addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if (task.isSuccessful()){
//                        count++;
//                        clearAll();
//                    }
//                }
//            });
        }

//        if (!employee_id.equals("") && !employee_role.equals("") && !employee_division.equals("") &&
//                answer2.getAssessment_id() != null && answer2.getTopic_id() != null && answer2.getValue() != null){
//            databaseReference.child("t_appraisal").child(employee_division).child(performance_id).child("value").push().setValue(answer2).addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if (task.isSuccessful()){
//                        finish();
//                    }
//                }
//            });
//        }
    }

    private void saveAndVisible() {
    }

    private void clearAll(){
        cvAnswer1.setCardBackgroundColor(ContextCompat.getColor(QuestionnaireActivity.this, R.color.white));
        cvAnswer2.setCardBackgroundColor(ContextCompat.getColor(QuestionnaireActivity.this, R.color.white));
        cvAnswer3.setCardBackgroundColor(ContextCompat.getColor(QuestionnaireActivity.this, R.color.white));
        cvAnswer4.setCardBackgroundColor(ContextCompat.getColor(QuestionnaireActivity.this, R.color.white));
        cvAnswer5.setCardBackgroundColor(ContextCompat.getColor(QuestionnaireActivity.this, R.color.white));
        answer = "";
        if (count == (questions.size() - 1)){
            fabNext.setVisibility(View.GONE);
            fabFinish.setVisibility(View.VISIBLE);
        }
    }

    private void saveAnswer() {
        employee_id = sharedPreferences.getString("employee_id", "");
        employee_role = sharedPreferences.getString("role", "");
        employee_division = sharedPreferences.getString("division", "");
        Answer answer2 = new Answer();
        answer2.setEmployee_id(employee_id);
        answer2.setAssessment_id(questionsKey.get(count));
        answer2.setTopic_id(topic_id);
        answer2.setValue(answer);
        Log.d(TAG, "saveAnswer: " + answer2.getValue());

        if (!employee_id.equals("") && !employee_role.equals("") && !employee_division.equals("") &&
        answer2.getAssessment_id() != null && answer2.getTopic_id() != null && answer2.getValue() != null){
            databaseReference.child("t_appraisal").child(employee_division).child(performance_id).child("value").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        answers.clear();
                        for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                            Log.d(TAG, "onDataChang22: " + dataSnapshot.getValue().toString());
                            Answer answerSnapshot = dataSnapshot.getValue(Answer.class);
                            answerSnapshot.setKey(dataSnapshot.getKey());
                            answers.add(answerSnapshot);
                        }
                        String key = null;
                        if (answers.stream().anyMatch(answer1 -> answer1.getAssessment_id().equals(answer2.getAssessment_id()) &&
                                        answer1.getEmployee_id().equals(answer2.getEmployee_id()) && answer1.getTopic_id().equals(answer2.getTopic_id()))){
                            key = answers.stream().filter(answer1 -> answer1.getAssessment_id().equals(answer2.getAssessment_id()) &&
                                            answer1.getEmployee_id().equals(answer2.getEmployee_id()) && answer1.getTopic_id().equals(answer2.getTopic_id()))
                                    .findFirst().get().getKey();
                        }
                        if (key != null){
                            databaseReference.child("t_appraisal").child(employee_division).child(performance_id).child("value").child(key).setValue(answer2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        count++;
                                        clearAll();
                                    }
                                }
                            });
                        } else {
                            databaseReference.child("t_appraisal").child(employee_division).child(performance_id).child("value").push().setValue(answer2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        count++;
                                        clearAll();
                                    }
                                }
                            });
                        }
                    } else {
                        databaseReference.child("t_appraisal").child(employee_division).child(performance_id).child("value").push().setValue(answer2).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    count++;
                                    clearAll();
                                }
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "onCancelled: ", error.toException());
                }
            });
//            databaseReference.child("t_appraisal").child(employee_division).child(performance_id).child("value").push().setValue(answer2).addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if (task.isSuccessful()){
//                        count++;
//                        clearAll();
//                    }
//                }
//            });
        }
    }
}