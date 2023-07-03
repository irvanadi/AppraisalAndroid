package com.example.appraisalandroid.Page.Admin.Dashboard.Data;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.appraisalandroid.Adapter.DivisionAdapter;
import com.example.appraisalandroid.Adapter.TopicAdapter;
import com.example.appraisalandroid.Models.Division;
import com.example.appraisalandroid.Models.Topic;
import com.example.appraisalandroid.R;
import com.example.appraisalandroid.Utils.RecyclerViewInstance;
import com.google.android.material.divider.MaterialDividerItemDecoration;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TopicActivity extends AppCompatActivity {

    private RecyclerView rvTopic;
    private DatabaseReference databaseReference;
    private TextInputEditText txtInputTopic;
    private TextInputLayout txtLayoutTopic;
    private ArrayList<Topic> topics = new ArrayList<>();
    private TopicAdapter topicAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        getSupportActionBar().setTitle("Topic");
        rvTopic = findViewById(R.id.rvTopic);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        findViewById(R.id.fabAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu();
            }
        });

        initRecyclerView();
        loadData();
    }

    private void initRecyclerView() {
        MaterialDividerItemDecoration materialDividerItemDecoration = new MaterialDividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        materialDividerItemDecoration.setLastItemDecorated(false);
        materialDividerItemDecoration.setDividerInsetStart(3);
        RecyclerViewInstance.getRecyclerViewVertical(rvTopic, this);
        rvTopic.addItemDecoration(materialDividerItemDecoration);
        topicAdapter = new TopicAdapter(this, topics);
        rvTopic.setAdapter(topicAdapter);
    }

    private void loadData() {
        databaseReference.child("topics").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    topics.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Topic topic = dataSnapshot.getValue(Topic.class);
                        topic.setKey(dataSnapshot.getKey());
                        topics.add(topic);
                    }
                    topicAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: ", error.toException());
            }
        });
    }

    private void PopupMenu(){
        final AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("Topic");

        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.pop_up_division, null);
        txtInputTopic = view.findViewById(R.id.txtEditDivision);
        txtLayoutTopic = view.findViewById(R.id.txtLayoutDivision);
        txtLayoutTopic.setHint("Topic");
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();

        view.findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Topic topic = new Topic(txtInputTopic.getText().toString());
                databaseReference.child("topics").push().setValue(topic);
                dialog.dismiss();
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.add_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.add:
//                PopupMenu();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
}