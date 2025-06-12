package com.example.viadee_coderetreat_app.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.example.viadee_coderetreat_app.ui.register_and_login.LoginActivity;
import com.example.viadee_coderetreat_app.ui.register_and_login.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;

/**
 * Provides user registration functionality using Firebase Authentication and Realtime Database.
 */
public class RegistrationService {

    /**
     * Registers a new user with Firebase Authentication and stores additional user data
     * in Firebase Realtime Database.
     *
     * <p>
     * This method creates a user account using the provided email and password.
     * Upon successful registration, it also stores the user's profile information
     * in the Realtime Database under the "Users" node.
     * If registration is successful, the user is redirected to the login activity.
     * If an error occurs (e.g. email already exists), appropriate feedback is shown.
     * </p>
     *
     * @param context   The activity context used for UI operations and launching intents.
     * @param auth      The FirebaseAuth instance used to create the user.
     * @param dbRef     The reference to the Firebase Realtime Database where user data is stored.
     * @param fName     The user's first name.
     * @param lName     The user's last name.
     * @param username  The user's chosen username.
     * @param email     The user's email address.
     * @param password  The user's password.
     * @param id        A custom user ID to be associated with the user (not the Firebase UID).
     */
    public static void registerUser(Activity context,
                                    FirebaseAuth auth,
                                    DatabaseReference dbRef,
                                    String fName,
                                    String lName,
                                    String username,
                                    String email,
                                    String password,
                                    String id) {

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Register", "Firebase user created.");
                        String uid = auth.getCurrentUser().getUid();
                        User newUser = new User(fName, lName, username, email, id);

                        dbRef.child(uid).setValue(newUser)
                                .addOnCompleteListener(dbTask -> {
                                    if (dbTask.isSuccessful()) {
                                        Log.d("Register", "User data saved to Realtime Database.");
                                        Toast.makeText(context, "User saved!", Toast.LENGTH_LONG).show();
                                        context.startActivity(new Intent(context, LoginActivity.class));
                                    } else {
                                        Exception e = dbTask.getException();
                                        Log.e("Register", "Database error", e);
                                        Toast.makeText(context, "Failed to save user: " +
                                                (e != null ? e.getMessage() : "Unknown error"), Toast.LENGTH_LONG).show();
                                    }
                                });

                    } else {
                        Exception e = task.getException();
                        if (e instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(context, "Email already in use.", Toast.LENGTH_LONG).show();
                        } else {
                            Log.e("Register", "Firebase Auth error", e);
                            Toast.makeText(context, "Auth failed: " +
                                    (e != null ? e.getMessage() : "Unknown error"), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
