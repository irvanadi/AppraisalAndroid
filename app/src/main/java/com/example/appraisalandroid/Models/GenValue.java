package com.example.appraisalandroid.Models;

import androidx.annotation.Nullable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class GenValue {

    public ArrayList<GenerateValue> generateValues;
    public String period_id, key, status;
    public Period period;
    public double consistency;

    public GenValue(){}
    public GenValue(ArrayList<GenerateValue> generateValues, @Nullable double consistency, @Nullable String period_id) {
        this.generateValues = generateValues;
        this.consistency = consistency;
        this.period_id = period_id;
    }
    public GenValue(double consistency, Period period){
        this.consistency = consistency;
        this.period = period;
    }
    public GenValue(ArrayList<GenerateValue> generateValues, double consistency, Period period) {
        this.generateValues = generateValues;
        this.consistency = consistency;
        this.period = period;
    }

    public GenValue(ArrayList<GenerateValue> generateValues) {
        this.generateValues = generateValues;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getConsistency() {
        return consistency;
    }

    public void setConsistency(double consistency) {
        this.consistency = consistency;
    }

    public String getPeriod_id() {
        return period_id;
    }

    public void setPeriod_id(String period_id) {
        this.period_id = period_id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ArrayList<GenerateValue> getGenerateValues() {
        return generateValues;
    }

    public void setGenerateValues(ArrayList<GenerateValue> generateValues) {
        this.generateValues = generateValues;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }
}
