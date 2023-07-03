package com.example.appraisalandroid.Page.Admin.Dashboard.Data.Employee;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appraisalandroid.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddEmployee2Fragment} factory method to
 * create an instance of this fragment.
 */
public class AddEmployee2Fragment extends Fragment {

    public AddEmployee2Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_employee2, container, false);
        return view;
    }
}