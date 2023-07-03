package com.example.appraisalandroid.Models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@IgnoreExtraProperties
public class Period {

    public Long start, end;
    public String key, name;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

    public Period(){}

    public Period(Long start, Long end, String name) {
        this.start = start;
        this.end = end;
        this.name = name;
    }

    public Long getStart() {
        return start;
    }

    public String getDateStart() {
        return simpleDateFormat.format(start);
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getEnd() {
        return end;
    }

    public String getDateEnd() {
        return simpleDateFormat.format(end);
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
