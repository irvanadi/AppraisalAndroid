package com.example.appraisalandroid.Page.Admin.Dashboard;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appraisalandroid.Page.Admin.Dashboard.Data.GenerateActivity;
import com.example.appraisalandroid.Page.Admin.Dashboard.Data.PeriodActivity;
import com.example.appraisalandroid.Page.Admin.Dashboard.Data.DivisionActivity;
import com.example.appraisalandroid.Page.Admin.Dashboard.Data.EmployeeActivity;
import com.example.appraisalandroid.Page.Admin.Dashboard.Data.TopicActivity;
import com.example.appraisalandroid.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardFragment extends Fragment {

    private FirebaseAuth mFirebaseAuth;
    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        getActivity().setTitle("Dashboard");

        view.findViewById(R.id.cvEmployee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), EmployeeActivity.class));
            }
        });

        view.findViewById(R.id.cvDivision).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), DivisionActivity.class));
            }
        });

        view.findViewById(R.id.cvTopic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), TopicActivity.class));
            }
        });

        view.findViewById(R.id.cvGenerate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), GenerateActivity.class));
            }
        });

        view.findViewById(R.id.cvPeriod).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), PeriodActivity.class));
            }
        });

        return view;
    }


}