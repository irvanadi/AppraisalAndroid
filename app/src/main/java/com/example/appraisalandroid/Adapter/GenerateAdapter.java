package com.example.appraisalandroid.Adapter;

import static android.content.ContentValues.TAG;

import android.animation.LayoutTransition;
import android.content.Context;
import android.graphics.Color;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appraisalandroid.Models.GenValue;
import com.example.appraisalandroid.Models.GenerateValue;
import com.example.appraisalandroid.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

public class GenerateAdapter extends RecyclerView.Adapter<GenerateAdapter.ViewHolder>{

    private Context context;
    private ArrayList<GenValue> genValues;
    private DatabaseReference databaseReference;

    public GenerateAdapter(Context context, ArrayList<GenValue> genValues) {
        this.context = context;
        this.genValues = genValues;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_generate_llc, parent, false);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GenValue genValue = genValues.get(position);
        if (genValue.getPeriod() != null){
            holder.txtPeriodName.setText(genValue.getPeriod().getName());
            holder.txtDate.setText(genValue.getPeriod().getDateStart() + " - " + genValue.getPeriod().getDateEnd());
        }
        Log.d(TAG, "onBindViewHolder: " + genValues.size());
        Log.d(TAG, "onBindViewHolder: " + genValue.getKey());
        Date date = new Date();
        if (genValue.getStatus() != null && date.getTime() >= genValue.getPeriod().getStart() &&
        date.getTime() <= genValue.getPeriod().getEnd()){
            holder.txtStatus.setVisibility(View.VISIBLE);
        } else {
            holder.txtStatus.setVisibility(View.GONE);
        }
        databaseReference.child("gen_values").child(genValue.getKey()).child("topics").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    holder.linearDetail.removeAllViews();
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                        GenerateValue generateValue = dataSnapshot.getValue(GenerateValue.class);
                        TextView textView = new TextView(context);
                        textView.setText(generateValue.getTopic_name() + ": " + String.format("%.2f", generateValue.getValue()));
                        textView.setTextColor(Color.BLACK);
                        holder.linearDetail.addView(textView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: ", error.toException());
            }
        });
        holder.rlGenerate.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
//        databaseReference.child()
//        if (genValue.getGenerateValues() != null){
//            for (int i = 0; i < genValue.getGenerateValues().size(); i++){
//                TextView textView = new TextView(context);
//                GenerateValue generateValue = genValue.getGenerateValues().get(i);
//                textView.setId(i);
//                textView.setText(generateValue.getTopic_name());
//                holder.linearDetail.addView(textView);
//                Log.d(TAG, "onBindViewHolder: " + generateValue.getTopic_name());
//            }
//        }
        holder.ivExpandable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.linearDetail.getVisibility() == View.VISIBLE){
                    TransitionManager.beginDelayedTransition(holder.rlGenerate, new AutoTransition());
                    holder.linearDetail.setVisibility(View.GONE);
                    holder.ivExpandable.setImageResource(R.drawable.ic_down);
                } else {
                    TransitionManager.beginDelayedTransition(holder.rlGenerate, new AutoTransition());
                    holder.linearDetail.setVisibility(View.VISIBLE);
                    holder.ivExpandable.setImageResource(R.drawable.ic_up);
                }
            }
        });
//        holder.rlGenerate.addView(view);
    }


    @Override
    public int getItemCount() {
        return genValues.size();
    }

//    private void expandable(ViewHolder holder) {
//        int v = (holder.detailText.getVisibility() == View.GONE)? View.VISIBLE : View.GONE;
//        int s = (holder.detailText.getVisibility() == View.GONE)? holder.ivExpandable.setImageResource(R.drawable.ic_down);
//
//        TransitionManager.beginDelayedTransition(holder.rlGenerate, new AutoTransition());
//    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtPeriodName, txtDate, detailText, txtStatus;
        RelativeLayout rlGenerate;
        LinearLayoutCompat linearDetail;
        ImageButton ivExpandable;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPeriodName = itemView.findViewById(R.id.txtName);
            txtDate = itemView.findViewById(R.id.txtDate);
            rlGenerate = itemView.findViewById(R.id.rlGenerate);
            ivExpandable = itemView.findViewById(R.id.ivExpandable);
            detailText = itemView.findViewById(R.id.detailText);
            linearDetail = itemView.findViewById(R.id.linearDetail);
            txtStatus = itemView.findViewById(R.id.txtStatus);
        }
    }
}
