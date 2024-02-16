package com.example.agevoyage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.agevoyage.databinding.ActivityLoginPageBinding;

public class loginPage extends AppCompatActivity {

    ActivityLoginPageBinding binding;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=binding.loginemail.getText().toString();
                String password=binding.loginpassword.getText().toString();

                if (email.equals("") || password.equals(""))
                    Toast.makeText(loginPage.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                else {
                    Boolean checkCredentials=databaseHelper.checkEmailPassword(email,password);

                    if (checkCredentials==true){
                        Toast.makeText(loginPage.this, "Login successfull", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(loginPage.this, "Invalid credentials", Toast.LENGTH_SHORT).show();


                    }
                }
            }
        });
        binding.signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(loginPage.this,signup.class);
                startActivity(intent);
            }
        });
    }
}