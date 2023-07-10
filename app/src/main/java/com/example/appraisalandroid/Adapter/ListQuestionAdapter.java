package com.example.appraisalandroid.Adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appraisalandroid.Models.Assessment;
import com.example.appraisalandroid.Models.Topic;
import com.example.appraisalandroid.Page.Admin.Dashboard.Data.Employee.AddEmployee2Fragment;
import com.example.appraisalandroid.Page.Employee.Appraisal.QuestionnaireActivity;
import com.example.appraisalandroid.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListQuestionAdapter extends RecyclerView.Adapter<ListQuestionAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Topic> topics;
    private ArrayList<Assessment> assessments2;
    private DatabaseReference databaseReference;

    public ListQuestionAdapter(Context context, ArrayList<Topic> topics) {
        this.context = context;
        this.topics = topics;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_question, parent, false);
        ViewHolder holder = new ViewHolder(v);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Topic topic = topics.get(position);
//        databaseReference.child("assessments").child(topic.getKey()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()){
//                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//                        Assessment assessment = dataSnapshot.getValue(Assessment.class);
//                        assessments2.add(assessment);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e(TAG, "onCancelled: ", error.toException());
//            }
//        });
        if (topic.getName() != null) holder.txtTopic.setText(topic.getName());
        if (topic.getAssessments() != null){
            for (Assessment assessment: topic.getAssessments()){
                Log.d(TAG, "onBindViewHolder: " + position + " " + assessment.getName());
            }
            if (topic.getAssessments().size() > 1){
                holder.txtTotalQuestion.setText(String.valueOf(topic.getAssessments().size()) + " Questions");
            } else {
                holder.txtTotalQuestion.setText(String.valueOf(topic.getAssessments().size()) + " Question");
            }
        } else {
            holder.txtTotalQuestion.setText("0 Question");
        }
        holder.cvListQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (topic.getAssessments() != null && topic.getAssessments().size() >= 1){
//                    String questions[] = new String[topic.getAssessments().size()];
                    ArrayList<String> questions = new ArrayList<>();
                    ArrayList<String> questionsKey = new ArrayList<>();
                    for (Assessment assessment: topic.getAssessments()){
                        questions.add(assessment.getName());
                        questionsKey.add(assessment.getKey());
                    }
                    Intent intent = new Intent(context, QuestionnaireActivity.class);
                    intent.putExtra("performance_id", topic.getPerformance_id());
                    intent.putStringArrayListExtra("assessments", questions);
                    intent.putStringArrayListExtra("assessmentsKey", questionsKey);
                    intent.putExtra("assessmentsList", topic.getAssessments());
                    intent.putExtra("topic_id", topic.getKey());
                    intent.putExtra("generate_id", topic.getGenerate_id());
                    context.startActivity(intent);
                }
            }
        });
        Log.d(TAG, "onBindViewHolder: " + topics.size());
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTopic, txtTotalQuestion;
        CardView cvListQuestion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTopic = itemView.findViewById(R.id.txtTopic);
            txtTotalQuestion = itemView.findViewById(R.id.txtTotalQuestion);
            cvListQuestion = itemView.findViewById(R.id.cvListQuestion);
        }
    }
}
