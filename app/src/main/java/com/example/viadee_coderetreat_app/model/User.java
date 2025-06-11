package com.example.viadee_coderetreat_app.ui.register_and_login;

public  class User {
    public String email, firstName,lastName,id,username;

    public User() {}  // Required by Firebase

    public User( String firstName, String lastName, String username, String email,String id) {
        this.email = email;
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username= username;
    }
}

