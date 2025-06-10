package com.example.viadee_coderetreat_app.ui.register_and_login;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.viadee_coderetreat_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText userName, Password, Email, ID, firstName, lastName, passwordConfirmation;
    Button registerbtn;
    DatabaseReference dbRef;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Immersive Mode
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);

        // Animation
        ImageView logo = findViewById(R.id.logoImage);
        logo.animate().translationYBy(-600f).setDuration(1500).start();

        // Bind views
        userName = findViewById(R.id.userName);
        Password = findViewById(R.id.Password);
        passwordConfirmation = findViewById(R.id.passwordConfirmation);
        Email = findViewById(R.id.Email);
        ID = findViewById(R.id.ID);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        registerbtn = findViewById(R.id.registerbtn);

        dbRef = FirebaseDatabase.getInstance().getReference("Users");
        auth = FirebaseAuth.getInstance();

        registerbtn.setOnClickListener(view -> {
            Toast.makeText(this, "Register button clicked", Toast.LENGTH_SHORT).show();
            Log.d("Register", "Button clicked");

            String username = userName.getText().toString().trim();
            String password = Password.getText().toString().trim();
            String confirmPassword = passwordConfirmation.getText().toString().trim();
            String email = Email.getText().toString().trim();
            String id = ID.getText().toString().trim();
            String fName = firstName.getText().toString().trim();
            String lName = lastName.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()
                    || email.isEmpty() || id.isEmpty() || fName.isEmpty() || lName.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Creating user...", Toast.LENGTH_SHORT).show();
            Log.d("Register", "Creating user in Firebase Auth");

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Firebase Auth success", Toast.LENGTH_SHORT).show();
                            Log.d("Register", "Auth success");

                            UserData newUser = new UserData(password, email, id, fName, lName);

                            dbRef.child(username).setValue(newUser)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(this, "User saved in RTDB", Toast.LENGTH_LONG).show();
                                        Log.d("Register", "Saved to RTDB");
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "DB Save Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                        Log.e("Register", "RTDB Error", e);
                                    });
                        } else {
                            Exception e = task.getException();
                            if (e instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(this, "Email already in use.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(this, "Auth failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                            Log.e("Register", "Auth error", e);
                        }
                    });
        });
    }

    public static class UserData {
        public String password, email, id, firstName, lastName;

        public UserData() {} // Required by Firebase

        public UserData(String password, String email, String id, String firstName, String lastName) {
            this.password = password;
            this.email = email;
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
        }
    }
}


