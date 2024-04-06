package com.example.agevoyage;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginAdmin extends AppCompatActivity {

    private static final String DEFAULT_ADMIN_USERNAME = "admin";
    private static final String DEFAULT_ADMIN_PASSWORD = "admin";

    private EditText adminUsernameEditText, adminPasswordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);

        adminUsernameEditText = findViewById(R.id.adminlogin);
        adminPasswordEditText = findViewById(R.id.adminpass);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = adminUsernameEditText.getText().toString().trim();
                String password = adminPasswordEditText.getText().toString().trim();

                if (username.equals(DEFAULT_ADMIN_USERNAME) && password.equals(DEFAULT_ADMIN_PASSWORD)) {
                    // Admin login successful
                    // Redirect to admin dashboard or desired activity
                    Intent intent = new Intent(LoginAdmin.this, ManagePlace.class);
                    startActivity(intent);
                    finish(); // Finish the current activity to prevent going back to the login page
                } else {
                    // Admin login failed
                    Toast.makeText(LoginAdmin.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
