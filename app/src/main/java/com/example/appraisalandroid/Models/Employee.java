package com.example.appraisalandroid.Models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Employee {
    public String key, name, phone, email, password, division_id, role, generate_id;
    public Division division;

    public Employee(){};

    public Employee(String name, String phone, String email, String password, String division_id, String role) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.division_id = division_id;
        this.role = role;
    }

    public Employee(String name, String phone, String email, String password, Division division) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.division = division;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKey() {
        return key;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDivision_id() {
        return division_id;
    }

    public void setDivision_id(String division_id) {
        this.division_id = division_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Division getDivision() {
        return division;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    public String getGenerate_id() {
        return generate_id;
    }

    public void setGenerate_id(String generate_id) {
        this.generate_id = generate_id;
    }
}
