package com.example.viadee_coderetreat_app.firebase;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true); // Optional: enable offline support
    }
}
