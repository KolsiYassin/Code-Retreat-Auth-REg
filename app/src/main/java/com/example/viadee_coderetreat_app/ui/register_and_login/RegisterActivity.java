package com.example.viadee_coderetreat_app.ui.register_and_login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.viadee_coderetreat_app.R;
import com.example.viadee_coderetreat_app.services.RegistrationService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText userName, Password, Email, ID, firstName, lastName, passwordConfirmation;
    Button registerbtn ;
    ImageButton backbtn;
    DatabaseReference dbRef;
    FirebaseAuth auth;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
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

        // Logo animation
       // ImageView logo = findViewById(R.id.logoImage);
        //logo.animate().translationYBy(-600f).setDuration(1500).start();

        // Bind views
        userName = findViewById(R.id.userName);
        Password = findViewById(R.id.Password);
        passwordConfirmation = findViewById(R.id.passwordConfirmation);
        Email = findViewById(R.id.Email);
        ID = findViewById(R.id.ID);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        registerbtn = findViewById(R.id.registerbtn);
        backbtn = findViewById(R.id.backbtn);

        dbRef = FirebaseDatabase.getInstance().getReference("Users");
        auth = FirebaseAuth.getInstance();
        /// Back to login Page 
        backbtn.setOnClickListener(v->{
            startActivity(new Intent(this, LoginActivity.class));
        });
        
        /// registration

        registerbtn.setOnClickListener(view -> {
            Log.d("Register", "Register button clicked");

            String username = userName.getText().toString().trim();
            String password = Password.getText().toString().trim();
            String confirmPassword = passwordConfirmation.getText().toString().trim();
            String email = Email.getText().toString().trim();
            String id = ID.getText().toString().trim();
            String fName = firstName.getText().toString().trim();
            String lName = lastName.getText().toString().trim();
            /// empty field check

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()
                    || email.isEmpty() || id.isEmpty() || fName.isEmpty() || lName.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
                return;
            }
            /// save using Auth

            Log.d("Register", "Creating user in Firebase Auth");

            RegistrationService.registerUser(
                    this,               // Activity context
                    auth,               // FirebaseAuth instance
                    dbRef,              // DatabaseReference to "users"
                    fName,              // First name
                    lName,              // Last name
                    username,           // Username
                    email,              // Email
                    password,           // Password
                    id                  // Custom user ID
            );
        });
    }
}
