package com.example.agevoyage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.agevoyage.databinding.ActivitySignupBinding;

public class signup extends AppCompatActivity {

    ActivitySignupBinding binding;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseHelper = new DatabaseHelper(this);
        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.signupEmail.getText().toString();
                String password = binding.signupPassword.getText().toString();
                String confirmPassword = binding.signupconfirmPassword.getText().toString();

                // Validate email format
                if (!isValidEmail(email)) {
                    Toast.makeText(signup.this, "Invalid email format", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validate password format
                if (!isValidPassword(password)) {
                    Toast.makeText(signup.this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(signup.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Proceed with signup process
                Boolean checkUserEmail = databaseHelper.checkEmail(email);
                if (!checkUserEmail) {
                    // If email is not already registered, start the SendOtpActivity
                    Intent intent = new Intent(signup.this, SendOTPActivity.class);
                    startActivity(intent);
                    finish(); // Finish the current activity if needed
                } else {
                    Toast.makeText(signup.this, "User already exists, please login", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), loginPage.class);
                startActivity(intent);
            }
        });
    }

    // Email validation method
    public boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+(?:\\.[a-z]+)*";
        return email.matches(emailPattern);
    }

    // Password validation method
    public boolean isValidPassword(String password) {
        return password.length() >= 6;
    }
}
