package com.example.viadee_coderetreat_app.ui.register_and_login;

import android.os.Bundle;
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
import com.example.viadee_coderetreat_app.model.UserData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    EditText userName, Password, Email, ID, firstName, lastName, passwordConfirmation;
    Button registerbtn;
    DatabaseReference dbRef;

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

        // Full immersive mode
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);

        ImageView logo = findViewById(R.id.logoImage);
        logo.animate()
                .translationYBy(-600f)
                .setDuration(1500)
                .start();

        // Bind views
        userName = findViewById(R.id.userName);
        Password = findViewById(R.id.Password);
        passwordConfirmation = findViewById(R.id.passwordConfirmation);
        Email = findViewById(R.id.Email);
        ID = findViewById(R.id.ID);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        registerbtn = findViewById(R.id.registerbtn);

        // Firebase reference
        dbRef = FirebaseDatabase.getInstance().getReference("Users");

        registerbtn.setOnClickListener(view -> {
            String username = userName.getText().toString().trim();
            String password = Password.getText().toString().trim();
            String confirmPassword = passwordConfirmation.getText().toString().trim();
            String email = Email.getText().toString().trim();
            String id = ID.getText().toString().trim();
            String fName = firstName.getText().toString().trim();
            String lName = lastName.getText().toString().trim();

            // Check for empty fields
            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ||
                    email.isEmpty() || id.isEmpty() || fName.isEmpty() || lName.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if passwords match
            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if username exists
            dbRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Toast.makeText(RegisterActivity.this, "Username is already used.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Save new user
                        UserData newUser = new UserData(password, email, id, fName, lName);
                        dbRef.child(username).setValue(newUser)
                                .addOnSuccessListener(aVoid ->
                                        Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e ->
                                        Toast.makeText(RegisterActivity.this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(RegisterActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    // Simple user model


}
