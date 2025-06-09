package com.example.viadee_coderetreat_app.model;

public class UserData {
    public String password, email, id, firstName, lastName;

    public UserData() {}

    public UserData(String password, String email, String id, String firstName, String lastName) {
        this.password = password;
        this.email = email;
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}

