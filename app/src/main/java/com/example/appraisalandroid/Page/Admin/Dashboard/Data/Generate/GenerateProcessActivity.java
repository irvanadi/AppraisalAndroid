package com.example.appraisalandroid.Page.Admin.Dashboard.Data.Generate;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appraisalandroid.Adapter.GenerateProcessAdapter;
import com.example.appraisalandroid.Models.Appraisal;
import com.example.appraisalandroid.Models.Employee;
import com.example.appraisalandroid.Models.GenerateValue;
import com.example.appraisalandroid.Models.Period;
import com.example.appraisalandroid.Models.Topic;
import com.example.appraisalandroid.Page.Admin.Dashboard.Data.GenerateActivity;
import com.example.appraisalandroid.R;
import com.example.appraisalandroid.Utils.RecyclerViewInstance;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealVector;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GenerateProcessActivity extends AppCompatActivity {

    private RecyclerView rvTopics;
    private DatabaseReference databaseReference;
    private GenerateProcessAdapter generateProcessAdapter;
    private ArrayList<Topic> topics = new ArrayList<>();
    private ArrayList<Topic> topicSelected = new ArrayList<>();
    private String asd,b;
    private ArrayList<GenerateValue> generateValues = new ArrayList<>();

    /**
     * Random Consistency Index
     */
    protected static double RI[] = {0.0, 0.0, 0.58, 0.9, 1.12, 1.24, 1.32, 1.41, 1.45, 1.49, 1.51, 1.48, 1.56, 1.57, 1.59};

    /**
     * The matrix
     */
    protected Array2DRowRealMatrix mtx;

    /**
     * The Array of Total Variabel
     */
    protected double totalMatrix[];

    /**
     * The Array of pVektor
     */
    protected double pVektor[];

    /**
     * The Array of Bobot (Weight)
     */
    protected double bobot[];

    /**
     * The Array of Eigen Value
     */
    protected double pointEigenValue[];

    /**
     * The matrix normalisasi
     */
    protected Array2DRowRealMatrix normalisasi;

    /**
     * Contains
     */
    protected double pairwiseComparisonArray[];

    /**
     * Number of alternatives
     */
    protected int nrAlternatives;

    /**
     * The resulting weights/priorities
     */
    protected double weights[];

    /**
     * Corresponds to the weights
     */
    protected String labels[] = null;

    /**
     *
     */
    protected EigenDecomposition evd;

    /**
     * Convenience array, i.e. comparisonIndices[length=NumberOfPairwiseComparisons][2]
     * Contains minimum number of comparisons.
     */
    protected int[][] comparisonIndices;

    /**
     * Index of the greatest Eigenvalue/ -vector
     */
    protected int evIdx = 0; // index of actual eigenvalue/-vector

    /**
     * Index of the CI
     */
    protected double cIndex;

    /**
     * Index of the CR
     */
    protected double cRatio;

    /**
     * Index of the Total Eigen Value
     */
    protected double totalEV;

    protected double geoMean;
    private LinearLayoutCompat llcValue;
    private ArrayList<EditText> editTexts = new ArrayList<>();
    private ArrayList<Period> periods = new ArrayList<Period>();
    private ArrayList<String> results = new ArrayList<>();
    private Spinner spnPeriod;
    private String selectedValue;
    private ArrayList<Employee> employees = new ArrayList<>();
    private ArrayList<Employee> employeesSelected = new ArrayList<>();
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_process);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        rvTopics = findViewById(R.id.rvTopics);

        findViewById(R.id.btnProcess).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculate();
            }
        });
        spnPeriod = findViewById(R.id.spnPeriod);
        spnPeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedValue = periods.get(i).getKey();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        initRecyclerView();
        loadData();
        getPeriod();
        getDataCBox();
//        createEmployeePerformance();
    }

    private void getPeriod() {
        databaseReference.child("periods").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    periods.clear();
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        Period period = dataSnapshot.getValue(Period.class);
                        period.setKey(dataSnapshot.getKey());
                        periods.add(period);
                        results.add(period.getName());
                    }
                    if (periods.size() <= 0){
                        Toast.makeText(GenerateProcessActivity.this, "Please input atleast 1 Period", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(GenerateProcessActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, results);
                spnPeriod.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: ", error.toException());
            }
        });
    }

    private void getDataCBox() {
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("status"));
    }

    private void initRecyclerView() {
        RecyclerViewInstance.getRecyclerViewVertical(rvTopics, this);
        generateProcessAdapter = new GenerateProcessAdapter(this, topics);
        rvTopics.setAdapter(generateProcessAdapter);
    }

    private void loadData() {
        databaseReference.child("topics").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    topics.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Topic topic = dataSnapshot.getValue(Topic.class);
                        topic.setKey(dataSnapshot.getKey());
                        topics.add(topic);
                    }
                    generateProcessAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: ", error.toException());
            }
        });
    }

    private void calculate() {
        if (topicSelected.size() > 2 && !selectedValue.equals("")) {
            createDialog();
        } else {
            Toast.makeText(this, "Please Select at least 3 Topics", Toast.LENGTH_SHORT).show();
        }
    }

    private void createDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_bottomsheet);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        llcValue = (LinearLayoutCompat) dialog.getWindow().findViewById(R.id.llcValue);

        Button btnSave = dialog.getWindow().findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (EditText editText : editTexts){
                    if (editText.getText().toString().equals("")){
                        Toast.makeText(GenerateProcessActivity.this, "Please fill all values", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (editText.getText().toString().equals("0")){
                        Toast.makeText(GenerateProcessActivity.this, "Please fill value minimum 1", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                savingValue();
                DatabaseReference dataGenerate = databaseReference.child("gen_values").push();
                if (cRatio <= 0.1){
                    dataGenerate.child("consistency").setValue(cRatio);
                    dataGenerate.child("period_id").setValue(selectedValue);
                    dataGenerate.child("status").setValue("active");
                    for (int i = 0; i < bobot.length; i++){
                        GenerateValue generateValue =  new GenerateValue(topicSelected.get(i).getName(), bobot[i], topicSelected.get(i).getKey());
                        dataGenerate.child("topics").push().setValue(generateValue);
                    }
                    createEmployeePerformance(dataGenerate.getKey());
                } else {
                    Snackbar snackbar = Snackbar.make(dialog.getWindow().findViewById(R.id.llcBottomSheet), "this Value is not consistent, Please make new Value", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
        addView();
    }

    private void createEmployeePerformance(String generate_id) {
        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference();
        mReference.child("divisions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    DatabaseReference orderingDivision = mReference.child("t_appraisal").push();
                    for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                        mReference.child("employees").orderByChild("division_id").equalTo(dataSnapshot.getKey())
                                .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                if (snapshot1.exists()){
                                    employees.clear();
                                    for (DataSnapshot dataSnapshot1 : snapshot1.getChildren()){
                                        Employee employee = dataSnapshot1.getValue(Employee.class);
                                        employee.setKey(dataSnapshot1.getKey());
                                        employees.add(employee);
                                    }
                                    if (employees.size() >= 4 ){
                                        if (employees.stream().anyMatch(employee -> employee.getRole() != null && employee.getRole().equals("2"))){
                                            Log.d(TAG, "onDataChangeTestCount: " + dataSnapshot.getKey() + " match");
                                            String division_id = dataSnapshot.getKey();
                                            Log.d(TAG, "onDataChangeTestKey: " + division_id);
                                            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
                                            databaseReference1.child("employees").orderByChild("division_id").equalTo(division_id).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                                    if (snapshot2.exists()){
                                                        employeesSelected.clear();
                                                        for (DataSnapshot dataSnapshot2: snapshot2.getChildren()){
                                                            Employee employee = dataSnapshot2.getValue(Employee.class);
                                                            employee.setKey(dataSnapshot2.getKey());
                                                            employeesSelected.add(employee);
                                                        }
                                                        Log.d(TAG, "onDataChange: " + employeesSelected.size());
                                                        for (Employee employee: employeesSelected){
                                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                                            Appraisal appraisal = new Appraisal(employee.getKey());
                                                            Log.d(TAG, "onDataChangeTestPerformancer: " + appraisal.getEmployee_id());
                                                            DatabaseReference refPerform = reference.child("t_appraisal").child(generate_id).child(division_id).child(appraisal.getEmployee_id());
                                                            refPerform.setValue(employee.getKey());
                                                            for (int i = 0; i < employeesSelected.size(); i++) {
                                                                if (!employeesSelected.get(i).getKey().equals(appraisal.getEmployee_id())){
                                                                    DatabaseReference refRater = refPerform.child("rater").push();
                                                                    refRater.child("rater_id").setValue(employeesSelected.get(i).getKey());
                                                                    refRater.child("role").setValue(employeesSelected.get(i).getRole());
                                                                    Log.d(TAG, "onDataChangeTestRater: " + employeesSelected.get(i).getKey() + " div: " + employeesSelected.get(i).getDivision_id());
                                                                }
                                                            }
                                                        }
                                                    }
                                                    dialog.dismiss();
                                                    Toast.makeText(GenerateProcessActivity.this, "Generate Value is Success", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(GenerateProcessActivity.this, GenerateActivity.class);
                                                    intent.putExtra("generate", "success");
                                                    startActivity(intent);
                                                    finish();
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    Log.e(TAG, "onCancelled: ", error.toException());
                                                }
                                            });
//                                            for (Employee employee : employees.stream().filter(employee -> employee)){
//                                                Appraisal appraisal = new Appraisal(employee.getKey());
//                                                Log.d(TAG, "onDataChangeTestCount" + appraisal.getEmployee_id() + ": " + employees.stream().filter(employee1 -> !employee1.getKey().equals(employee.getKey())).count());
//                                            }
                                        } else {
                                            Log.d(TAG, "onDataChangeTestCount: " + dataSnapshot.getKey() + " only more 4");
                                        }
                                    } else {
                                        Log.d(TAG, "onDataChangeTestCount: " + dataSnapshot.getKey() + " not match");
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e(TAG, "onCancelled: ", error.toException());
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: ", error.toException());
            }
        });
    }

    private void savingValue() {
        this.nrAlternatives = topicSelected.size();
        mtx = new Array2DRowRealMatrix(nrAlternatives, nrAlternatives);
        normalisasi = new Array2DRowRealMatrix(nrAlternatives, nrAlternatives);
        totalMatrix = new double[nrAlternatives];
        weights = new double[nrAlternatives];
        pVektor = new double[nrAlternatives];
        bobot = new double[nrAlternatives];
        pointEigenValue = new double[nrAlternatives];
        totalEV = 0.0;

        pairwiseComparisonArray = new double[getNrOfPairwiseComparisons()];
        comparisonIndices = new int[getNrOfPairwiseComparisons()][];
        for (int i = 0; i < getNrOfPairwiseComparisons(); i++) {
            comparisonIndices[i] = new int[2];
        }

        // only need diagonal 1, but set everything to 1.0
        for (int row = 0; row < nrAlternatives; row++) {
            for (int col = 0; col < nrAlternatives; col++) {
                mtx.setEntry(row, col, 1.0);
            }
        }

        double compArray[] = getPairwiseComparisonArray();
        for (int i = 0; i < editTexts.size(); i++) {
            Log.d(TAG, "savingValue: " + editTexts.get(i).getText().toString());
            compArray[i] = Double.parseDouble(editTexts.get(i).getText().toString());
        }
        setPairwiseComparisonArray(compArray);
    }

    private void addView() {
//        for (int i = 0; i < getNrOfPairwiseComparisons(); i++){
//            final View view = getLayoutInflater().inflate(R.layout.item_dynamic_text, null, false);
//            TextView txtValue = (TextView) view.findViewById(R.id.txtValue);
//            EditText edValue = (EditText) view.findViewById(R.id.edValue);
//
//            llcValue.addView(view);
//        }
        for (int i = 0; i < topicSelected.size(); i++){
            for (int j = i + 1; j < topicSelected.size(); j++) {
                final View view = getLayoutInflater().inflate(R.layout.item_dynamic_text, null, false);
                TextView txtValue = (TextView) view.findViewById(R.id.txtValue);
                EditText edValue = (EditText) view.findViewById(R.id.edValue);
                edValue.setInputType(InputType.TYPE_CLASS_NUMBER);
                edValue.setFilters(new InputFilter[] { new InputFilter.LengthFilter(1) });
                txtValue.setText("Importance of " + topicSelected.get(i).getName() + " compared to " + topicSelected.get(j).getName());
                llcValue.addView(view);
                editTexts.add(edValue);
            }
        }
    }

    /**
     *
     * @return the number of pairwise comparisons which have to be done by the user
     */
    public int getNrOfPairwiseComparisons() {
        return ((nrAlternatives - 1) * nrAlternatives) / 2;
    }

    /**
     *
     * @return the user input of the pairwise comparisons
     */
    public double[] getPairwiseComparisonArray() {
        return pairwiseComparisonArray;
    }

    /**
     * Set the pairwise comparison scores and calculate all relevant numbers
     * @param a
     */
    public void setPairwiseComparisonArray(double a[]) {
        int i = 0;
        for (int row = 0; row < nrAlternatives; row++) {
            for (int col = row + 1; col < nrAlternatives; col++) {
                // System.out.println(row + "/" + col + "=" + a[i]);
                mtx.setEntry(row, col, a[i]);
                mtx.setEntry(col, row, 1.0 / mtx.getEntry(row, col));
                comparisonIndices[i][0] = row;
                comparisonIndices[i][1] = col;
                i++;
            }
        }

        for (int row = 0; row < nrAlternatives; row++){
            for (int col = 0; col < nrAlternatives; col++) {
                totalMatrix[row] += mtx.getEntry(row,col);
            }
        }
        Log.d(TAG, "setPairwiseComparisonArray: " + mtx);

        normalisasi = mtx;

        for (int row = 0; row < nrAlternatives; row++){
            for (int col = 0; col < nrAlternatives; col++) {
                normalisasi.setEntry(row,col, mtx.getEntry(row,col) / totalMatrix[row]);
            }
        }

        for (int col = 0; col < nrAlternatives; col++){
            for (int row = 0; row < nrAlternatives; row++) {
                pVektor[col] += normalisasi.getEntry(row,col);
            }
        }

        Log.d(TAG, "setPairwiseComparisonArray Normalisasi: " + normalisasi);
        Log.d(TAG, "setPairwiseComparisonArray: " + pVektor);

        for (int k = 0; k < nrAlternatives; k++){
            bobot[k] = pVektor[k] / nrAlternatives;
            pointEigenValue[k] = bobot[k] * totalMatrix[k];
            totalEV += pointEigenValue[k];
            System.out.println(bobot[k]);
            System.out.println("EV :" + pointEigenValue[k]);
        }

        cIndex = (totalEV - nrAlternatives) / (nrAlternatives - 1);
        cRatio = cIndex / RI[nrAlternatives -1];

        System.out.println(cIndex);
        System.out.println(cRatio);

        evd = new EigenDecomposition(normalisasi);

        evIdx = 0;
        for (int k = 0; k < evd.getRealEigenvalues().length; k++) {
            System.out.println(evd.getRealEigenvalues()[k]);
            evIdx = (evd.getRealEigenvalue(k) > evd.getRealEigenvalue(evIdx)) ? k : evIdx;
            System.out.println("evIdx=" + evIdx);
            System.out.println("EigenValue=" + evd.getRealEigenvalue(evIdx));
        }

        double sum = 0.0;
        RealVector v = evd.getEigenvector(evIdx);
        for (double d : v.toArray()) {
            sum += d;
        }

        System.out.println(sum);
        for (int k = 0; k < v.getDimension(); k++) {
            weights[k] = v.getEntry(k) / sum;
        }
    }

    /**
     *
     * @param arrayIdx
     * @return
     */
    public int[] getIndicesForPairwiseComparison(int arrayIdx) {
        return comparisonIndices[arrayIdx];
    }

    /**
     *
     * @return resulting weights for alternatives
     */
    public double[] getWeights() {
        return weights;
    }

    /**
     *
     * @return the consistency index
     */
    public double getConsistencyIndex() {
        return (evd.getRealEigenvalue(evIdx) - (double) nrAlternatives) / (double) (nrAlternatives - 1);
    }

    /**
     *
     * @return the consistency ratio. Should be less than 10%
     */
    public double getConsistencyRatio() {
        System.out.println(RI[nrAlternatives-1]);
        return getConsistencyIndex() / RI[nrAlternatives-1];
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<nrAlternatives; i++)
            sb.append(mtx.getRowVector(i) + "\n");
        return sb.toString();
    }

    public double getGeomean(int arr[], int n){
        int base = 0;
        for (int i = 0; i < n; i++){
            base *= arr[i];
        }

        double gm = Math.pow(base,1/n);
        return gm;
    }

    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            asd = intent.getStringExtra("status");
            b = intent.getStringExtra("key");
            Log.d(TAG, "onGenerateAct1: " + asd);
            Log.d(TAG, "onGenerateAct1: " + b);
            if (intent.getStringExtra("status").equalsIgnoreCase("y")){
                Topic topic = new Topic();
                topic.setName(intent.getStringExtra("name"));
                topic.setKey(intent.getStringExtra("key"));
                if (!topicSelected.stream().anyMatch(topic1 -> topic1.getKey() == topic.getKey())){
                    Log.d(TAG, "onReceiveaaa: adding " + topic.getName());
                    topicSelected.add(topic);
                }
            } else {
                Topic topic = new Topic();
                topic.setName(intent.getStringExtra("name"));
                topic.setKey(intent.getStringExtra("key"));
                topicSelected.removeIf(topic1 -> topic1.getKey().equalsIgnoreCase(topic.getKey()));
                Log.d(TAG, "onReceiveaaa: remove " + topic.getName());
            }
            Log.d(TAG, "onReceiveaaa: length" + topicSelected.size());
        }
    };

//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        TextView txtTopic = view.findViewById(R.id.txtTopic);
//        CheckBox cbTopic = view.findViewById(R.id.cbTopic);
//        cbTopic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (compoundButton.isChecked()){
//                    Log.d(TAG, "onCheckedChanged2: Y");
//                } else {
//                    Log.d(TAG, "onCheckedChanged2: N");
//                }
//            }
//        });
//    }
}