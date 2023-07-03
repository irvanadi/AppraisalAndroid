package com.example.appraisalandroid.Adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appraisalandroid.Models.Topic;
import com.example.appraisalandroid.R;

import java.util.ArrayList;

public class GenerateProcessAdapter extends RecyclerView.Adapter<GenerateProcessAdapter.ViewHolder> {

    Context context;
    ArrayList<Topic> topics;

    public GenerateProcessAdapter(Context context, ArrayList<Topic> topics) {
        this.context = context;
        this.topics = topics;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_generate_topic, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Topic topic = topics.get(position);
        holder.txtTopic.setText(topic.getName());
        holder.cbTopic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Intent intent = new Intent("status");
                if (compoundButton.isChecked()){
                    Log.d(TAG, "onCheckedChanged1: Y");
                    intent.putExtra("status", "Y");
                    intent.putExtra("name", topic.getName());
                    intent.putExtra("key", topic.getKey());
                } else {
                    Log.d(TAG, "onCheckedChanged1: N");
                    intent.putExtra("status", "N");
                    intent.putExtra("name", topic.getName());
                    intent.putExtra("key", topic.getKey());
                }
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTopic;
        CheckBox cbTopic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTopic = itemView.findViewById(R.id.txtTopic);
            cbTopic = itemView.findViewById(R.id.cbTopic);
        }
    }
}
