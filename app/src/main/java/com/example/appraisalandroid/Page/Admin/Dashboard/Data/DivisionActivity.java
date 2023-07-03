package com.example.appraisalandroid.Page.Admin.Dashboard.Data;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.appraisalandroid.Adapter.DivisionAdapter;
import com.example.appraisalandroid.Models.Division;
import com.example.appraisalandroid.R;
import com.example.appraisalandroid.Utils.RecyclerViewInstance;
import com.google.android.material.divider.MaterialDividerItemDecoration;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DivisionActivity extends AppCompatActivity {

    private RecyclerView rvDivision;
    private DatabaseReference databaseReference;
    private TextInputEditText txtInputDivision;
    private DivisionAdapter divisionAdapter;
    private ArrayList<Division> divisions = new ArrayList<>();
    private ExtendedFloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_division);

        getSupportActionBar().setTitle("Division");
//        Toolbar toolbar = findViewById(R.id.tbDivision);
//        setSupportActionBar(toolbar);
        rvDivision = findViewById(R.id.rvDivision);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
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
        RecyclerViewInstance.getRecyclerViewVertical(rvDivision, this);
        rvDivision.addItemDecoration(materialDividerItemDecoration);
        divisionAdapter = new DivisionAdapter(this, divisions);
        rvDivision.setAdapter(divisionAdapter);
    }

    private void loadData() {
        databaseReference.child("divisions").orderByValue().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    divisions.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Division division = dataSnapshot.getValue(Division.class);
                        divisions.add(division);
                    }
                    divisionAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: ", error.toException());
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

    private void PopupMenu(){
        final AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("Division");

        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.pop_up_division, null);
        txtInputDivision = view.findViewById(R.id.txtEditDivision);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();

        view.findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Division division = new Division(txtInputDivision.getText().toString());
                databaseReference.child("divisions").push().setValue(division);
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}