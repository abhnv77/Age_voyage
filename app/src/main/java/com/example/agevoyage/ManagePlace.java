package com.example.agevoyage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
public class ManagePlace extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_place);

        // Find the CardView by its ID
        CardView addCard = findViewById(R.id.add_card);
        CardView cardlogout = findViewById(R.id.cardlogout);
        CardView cardview = findViewById(R.id.viewcard);

        // Set OnClickListener for the CardView
        addCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform your desired action here, like launching activity_login_admim_add
                Intent intent = new Intent(ManagePlace.this, LoginAdmim_Add.class);
                startActivity(intent);
            }
        });
        cardlogout.setOnClickListener(new View.OnClickListener() {  //logout admin
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagePlace.this, loginPage.class);
                startActivity(intent);
            }
        });

        cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagePlace.this, login_admin_view.class);
                startActivity(intent);
            }
        });
    }
}
