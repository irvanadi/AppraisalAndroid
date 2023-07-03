package com.example.appraisalandroid.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appraisalandroid.Models.Division;
import com.example.appraisalandroid.R;

import java.util.ArrayList;

public class DivisionAdapter extends RecyclerView.Adapter<DivisionAdapter.ViewHolder> {

    Context context;
    ArrayList<Division> divisions;

    public DivisionAdapter(Context context, ArrayList<Division> divisions) {
        this.context = context;
        this.divisions = divisions;
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
        Division division = divisions.get(position);
        holder.txtDivisionName.setText(division.getName());
    }

    @Override
    public int getItemCount() {
        return divisions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtDivisionName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDivisionName = itemView.findViewById(R.id.txtDivisionName);
        }
    }
}
