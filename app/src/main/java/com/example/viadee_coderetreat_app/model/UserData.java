package com.example.viadee_coderetreat_app.ui.register_and_login;

public  class UserData {
    public String password;  // Stored as "secured_by_firebase" only
    public String email;
    public String id;
    public String firstName;
    public String lastName;

    public UserData() {}  // Required by Firebase

    public UserData( String email, String id, String firstName, String lastName) {
        //this.password = password;
        this.email = email;
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}

