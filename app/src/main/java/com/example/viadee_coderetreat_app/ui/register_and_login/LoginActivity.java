package com.example.viadee_coderetreat_app.ui.register_and_login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.viadee_coderetreat_app.R;
import com.example.viadee_coderetreat_app.services.AuthService;
import com.example.viadee_coderetreat_app.ui.home.MainActivity;




public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //standard onCreate function
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // completely immersive (no time and battery, just for login and reg, i find it better)
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);

        //Get UI
        TextView emailTextView = findViewById(R.id.name_edit_text);
        TextView passwordTextView = findViewById(R.id.password_edit_text);
        Button login = findViewById(R.id.Login);
        Button register = findViewById(R.id.register);

        // Slide up logo
        ImageView logo = findViewById(R.id.logoImage);
        logo.animate()
                .translationYBy(-600f)
                .setDuration(1500)
                .withEndAction(() -> {
                    // Show UI
                    emailTextView.setVisibility(View.VISIBLE);
                    passwordTextView.setVisibility(View.VISIBLE);
                    login.setVisibility(View.VISIBLE);
                    register.setVisibility(View.VISIBLE);
                })
                .start();

        // Login button listener
        login.setOnClickListener(v -> {
            String input = emailTextView.getText().toString().trim();
            String password = passwordTextView.getText().toString().trim();
            AuthService authservice = new AuthService(this);
            authservice.login(input,password,
                    //callbacks
                    () -> { //onSuccess
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    },
                    () -> { //onFailure
                        Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
                    }
            );
        });
        // Register Button Listener
        register.setOnClickListener(v->{
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }
}