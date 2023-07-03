package com.example.appraisalandroid.Models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class GenerateValue {

    public String key, topic_name, topic_id;
    public double value;

    public GenerateValue() {
    }

    public GenerateValue(String topic_name, double value, String topic_id) {
        this.topic_name = topic_name;
        this.value = value;
        this.topic_id = topic_id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTopic_name() {
        return topic_name;
    }

    public void setTopic_name(String topic_name) {
        this.topic_name = topic_name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(String topic_id) {
        this.topic_id = topic_id;
    }
}
