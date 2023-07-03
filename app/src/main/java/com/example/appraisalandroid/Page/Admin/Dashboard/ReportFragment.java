package com.example.appraisalandroid.Page.Admin.Dashboard;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appraisalandroid.Page.Admin.Dashboard.Data.DivisionActivity;
import com.example.appraisalandroid.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class ReportFragment extends Fragment {

    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;

    private CardView cvEmployee, cvDivision;
    public ReportFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        getActivity().setTitle("Data List");
//        setHasOptionsMenu(true);
//        mFirebaseAuth = FirebaseAuth.getInstance();

//        mDatabase = FirebaseDatabase.getInstance().getReference();
//        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
//        User user = new User(mFirebaseUser.getUid(), "admin", mFirebaseUser.getEmail());

//        mDatabase.child("user").setValue(user);

        view.findViewById(R.id.cvEmployee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        view.findViewById(R.id.cvDivision).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), DivisionActivity.class));
            }
        });
//        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (!task.isSuccessful()) {
//                    Log.e("firebase", "Error getting data", task.getException());
//                }
//                else {
//                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
//                }
//            }
//        });

        return view;
    }

//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        inflater.inflate(R.menu.add_menu, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.add:
//                Log.d("DashFragment", "onOptionsItemSelected: Add Selected");
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
}