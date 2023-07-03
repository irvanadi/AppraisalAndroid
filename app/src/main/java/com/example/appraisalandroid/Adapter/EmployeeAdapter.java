package com.example.appraisalandroid.Adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appraisalandroid.Models.Employee;
import com.example.appraisalandroid.R;

import java.util.ArrayList;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder>{

    Context context;
    ArrayList<Employee> employees;

    public EmployeeAdapter(Context context, ArrayList<Employee> employees) {
        this.context = context;
        this.employees = employees;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employee, parent, false);
        ViewHolder holder = new ViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Employee employee = employees.get(position);
        holder.txtName.setText(": " + employee.name);
        holder.txtEmail.setText(": " + employee.getEmail());
        holder.txtPhone.setText(": " + employee.getPhone());
        if (employee.division != null) holder.txtDivision.setText(": " + employee.division.getName());
    }

    @Override
    public int getItemCount() {
        return employees.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtEmail, txtDivision, txtPhone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtValueName);
            txtEmail = itemView.findViewById(R.id.txtValueEmail);
            txtDivision = itemView.findViewById(R.id.txtValueDivision);
            txtPhone = itemView.findViewById(R.id.txtValuePhone);
        }
    }
}
