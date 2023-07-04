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
import java.util.Collections;
import java.util.Comparator;

public class AppraisalAdapter extends RecyclerView.Adapter<AppraisalAdapter.ViewHolder>{
    private Context context;
    private ArrayList<Employee> employees;

    public AppraisalAdapter(Context context, ArrayList<Employee> employees) {
        this.context = context;
        this.employees = employees;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appraisal, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (employees.size() >= 1){
            employees.sort(Collections.reverseOrder(comparator));
        }
        Employee employee = employees.get(position);
        holder.txtName.setText(employee.name);
        if (employee.getRole() != null) {
            switch (employee.getRole()){
                case "1":
                    holder.txtRole.setText("Role: Employee");
                    break;
                case "2":
                    holder.txtRole.setText("Role: Leader");
                    break;
                case "3":
                    holder.txtRole.setText("Role: Admin");
                    break;
            }
        }
        Log.d(TAG, "onBindViewHolder: " + employee.getGenerate_id());
    }

    Comparator<Employee> comparator = new Comparator<Employee>() {
        @Override
        public int compare(Employee employee, Employee t1) {
            return employee.getRole().compareTo(t1.getRole());
        }
    };

    @Override
    public int getItemCount() {
        return employees.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtRole;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtRole = itemView.findViewById(R.id.txtRole);
        }
    }

}
