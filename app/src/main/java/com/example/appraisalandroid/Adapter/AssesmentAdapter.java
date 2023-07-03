package com.example.appraisalandroid.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appraisalandroid.Models.Assessment;
import com.example.appraisalandroid.R;

import java.util.ArrayList;

public class AssesmentAdapter extends RecyclerView.Adapter<AssesmentAdapter.ViewHolder>{

    Context context;
    ArrayList<Assessment> assessments;

    public AssesmentAdapter(Context context, ArrayList<Assessment> assessments) {
        this.context = context;
        this.assessments = assessments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_division, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Assessment assessment = assessments.get(position);
        holder.txtDivisionName.setText(assessment.getName());
    }

    @Override
    public int getItemCount() {
        return assessments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtDivisionName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDivisionName = itemView.findViewById(R.id.txtDivisionName);
        }
    }
}
