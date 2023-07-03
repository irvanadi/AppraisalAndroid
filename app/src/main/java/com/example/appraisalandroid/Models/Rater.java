package com.example.appraisalandroid.Models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Rater {

    public String key, rater_id, role;

    public Rater(){}

    public Rater(String rater_id, String role) {
        this.rater_id = rater_id;
        this.role = role;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRater_id() {
        return rater_id;
    }

    public void setRater_id(String rater_id) {
        this.rater_id = rater_id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
