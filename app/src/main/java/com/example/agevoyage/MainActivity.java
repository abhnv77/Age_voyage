 package com.example.agevoyage;


 import androidx.appcompat.app.AppCompatActivity;
 import androidx.recyclerview.widget.LinearLayoutManager;
 import androidx.recyclerview.widget.RecyclerView;

 import android.annotation.SuppressLint;
 import android.content.Intent;
 import android.os.Bundle;
 import android.view.View;
 import android.widget.Button;


 import com.example.agevoyage.databinding.ActivityLoginPageBinding;

 import java.util.ArrayList;

 public class MainActivity extends AppCompatActivity {

     RecyclerView touristdestinations, touristattractions;
     ArrayList<TouristDestinations_MODEL> touristDestinations_models;
     ArrayList<TouristAttractions_MODEL> touristAttractions_models;
     TouristDestinations_ADAPTER touristDestinations_adapter;
     TouristAttractions_ADAPTER touristAttractions_adapter;
     LinearLayoutManager manager;

     @SuppressLint("MissingInflatedId")
     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);

         Button startExploringButton = findViewById(R.id.button2);
         startExploringButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 // Open the login activity
                 Intent intent = new Intent(MainActivity.this, loginPage .class);
                 startActivity(intent);
             }
         });

         touristdestinations = findViewById(R.id.touristdestinations_recyclerview);

         touristDestinations_models = new ArrayList<>();
         touristDestinations_models.add(new TouristDestinations_MODEL(R.drawable.del, "New Delhi", "Delhi"));
         touristDestinations_models.add(new TouristDestinations_MODEL(R.drawable.agra_taj_mahal, "Jaipur", "Rajasthan"));
         touristDestinations_models.add(new TouristDestinations_MODEL(R.drawable.darjeeling, "Darjeeling", "West Bengal"));
         touristDestinations_models.add(new TouristDestinations_MODEL(R.drawable.varanasi, "Varanasi", "Uttar Pradesh"));

         touristDestinations_adapter = new TouristDestinations_ADAPTER(this, touristDestinations_models);
         manager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);

         touristdestinations.setAdapter(touristDestinations_adapter);
         touristdestinations.setLayoutManager(manager);



         touristattractions = findViewById(R.id.touristattractions_recyclerview);

         touristAttractions_models = new ArrayList<>();
         touristAttractions_models.add(new TouristAttractions_MODEL(R.drawable.agra_taj_mahal, "Taj Mahal", "Agra, Uttar Pradesh"));
         touristAttractions_models.add(new TouristAttractions_MODEL(R.drawable.amritsar, "Golden Temple", "Amritsar, Punjab"));
         touristAttractions_models.add(new TouristAttractions_MODEL(R.drawable.udaipur, "Lake Pichola", "Udaipur, Rajasthan"));
         touristAttractions_models.add(new TouristAttractions_MODEL(R.drawable.mysore, "Amba Vilas Palace", "Mysuru, Karnataka"));

         touristAttractions_adapter = new TouristAttractions_ADAPTER(this, touristAttractions_models);
         manager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);

         touristattractions.setAdapter(touristAttractions_adapter);
         touristattractions.setLayoutManager(manager);

     }
 }