package com.example.appraisalandroid.Models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class Topic {

    public String name, key, performance_id;
    public Assessment assessment;
    public ArrayList<Assessment> assessments;

    public Topic(){};
    public Topic(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Assessment getAssessment() {
        return assessment;
    }

    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }

    public ArrayList<Assessment> getAssessments() {
        return assessments;
    }

    public void setAssessments(ArrayList<Assessment> assessments) {
        this.assessments = assessments;
    }

    public String getPerformance_id() {
        return performance_id;
    }

    public void setPerformance_id(String performance_id) {
        this.performance_id = performance_id;
    }
}
