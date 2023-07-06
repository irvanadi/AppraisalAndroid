package com.example.appraisalandroid.Page.Employee.Appraisal;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;

import com.example.appraisalandroid.R;

public class QuestionerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questioner);
        Log.d(TAG, "onCreate: " + getIntent().getStringExtra("generate_id"));
        loadFragment(new ListQuestionFragment());
    }

    private boolean loadFragment(Fragment fragment) {
        // load fragment
        if (fragment != null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}