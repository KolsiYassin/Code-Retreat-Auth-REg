package com.example.viadee_coderetreat_app.ui.register_and_login;

public class UserData {
    public String password, email, id, firstName, lastName;

    public UserData() {}  // Required for Firebase

    public UserData(String password, String email, String id, String firstName, String lastName) {
        this.password = password;
        this.email = email;
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}

