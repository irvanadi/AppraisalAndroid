package com.example.appraisalandroid.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appraisalandroid.Models.Period;
import com.example.appraisalandroid.R;

import java.util.ArrayList;

public class PeriodAdapter extends RecyclerView.Adapter<PeriodAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Period> periods;

    public PeriodAdapter(Context context, ArrayList<Period> periods) {
        this.context = context;
        this.periods = periods;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_period, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Period period = periods.get(position);
        holder.txtName.setText(period.getName());
        holder.txtstartDate.setText(period.getDateStart());
        holder.txtEndDate.setText(period.getDateEnd());
    }

    @Override
    public int getItemCount() {
        return periods.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtstartDate, txtEndDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtstartDate = itemView.findViewById(R.id.txtstartDate);
            txtEndDate = itemView.findViewById(R.id.txtEndDate);
        }
    }
}
