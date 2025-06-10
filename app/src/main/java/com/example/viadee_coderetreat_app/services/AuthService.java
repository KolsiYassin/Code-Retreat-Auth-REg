package com.example.viadee_coderetreat_app.services;

import android.content.Context;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AuthService {
    private final FirebaseAuth mAuth;
    private final Context context;

    public AuthService(Context context) {
        this.mAuth = FirebaseAuth.getInstance();
        this.context = context;
    }

    /**
     * Attempts to log the user in using either an email address or a username and password.
     * <p>
     * If the input is a valid email address, it tries to sign in directly with that email.
     * If it's not an email, it attempts to resolve the username to an email in the database,
     * and then proceeds with email-based authentication.
     * </p>
     *
     * @param loginInput The user's email address or username.
     * @param password   The user's password.
     * @param onSuccess  A callback function to run if the login is successful.
     * @param onFailure  A callback function to run if the login fails, e.g., due to incorrect credentials or nonexistent user.
     */
    public void login(String loginInput, String password, Runnable onSuccess, Runnable onFailure) {
        if (Patterns.EMAIL_ADDRESS.matcher(loginInput).matches()) {
            // It's an email — login directly
            signInWithEmail(loginInput, password, onSuccess, onFailure);
        } else {
            //search user with the provided username to find the email
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
            usersRef.orderByChild("username")
                    .equalTo(loginInput)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                    String email = userSnapshot.child("email").getValue(String.class);
                                    if (email != null) {
                                        // email found, try to log in
                                        signInWithEmail(email, password, onSuccess, onFailure);
                                        return;
                                    }
                                }
                            } else {
                                //no username match
                                Toast.makeText(context, "Username not found", Toast.LENGTH_SHORT).show();
                                onFailure.run();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            Log.e("AuthService", "Database error: " + error.getMessage());
                            Toast.makeText(context, "Database error", Toast.LENGTH_SHORT).show();
                            onFailure.run();
                        }
                    });
        }
    }
    /**
     * Attempts to sign in the user using their email and password.
     * <p>
     * This is a helper method used internally (and only) by {@link #login(String, String, Runnable, Runnable)}.
     * If authentication is successful, the {@code onSuccess} callback is invoked;
     * otherwise, the {@code onFailure} callback is triggered.
     * </p>
     *
     * @param email      The user's email address.
     * @param password   The user's password.
     * @param onSuccess  A callback function to run if the login is successful.
     * @param onFailure  A callback function to run if the login fails.
     */

    private void signInWithEmail(String email, String password, Runnable onSuccess, Runnable onFailure) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Log.d("AuthService", "Signed in: " + (user != null ? user.getEmail() : "unknown"));
                        Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show();
                        onSuccess.run();
                    } else {
                        Log.w("AuthService", "Sign in failed", task.getException());
                        Toast.makeText(context, "Login failed: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                        onFailure.run();
                    }
                });
    }
}
