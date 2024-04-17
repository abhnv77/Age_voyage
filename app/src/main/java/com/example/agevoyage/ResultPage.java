package com.example.agevoyage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class ResultPage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseHelper databaseHelper;
    private ResultPage_ADAPTER resultPageAdapter;
    private ArrayList<String> placeDetailsList;
    private ArrayList<Bitmap> placeImages;
    private Button logoutButton,goToFiltersButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_page);

        recyclerView = findViewById(R.id.result_recycler_view);
        databaseHelper = new DatabaseHelper(this);
        placeDetailsList = new ArrayList<>();
        placeImages = new ArrayList<>();
        logoutButton = findViewById(R.id.logoutButton);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }

            private void logout() {
                Intent intent = new Intent(ResultPage.this, loginPage.class);
                startActivity(intent);
                finish();
            }
        });
        goToFiltersButton = findViewById(R.id.goToFiltersButton);
        goToFiltersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i= new Intent(ResultPage.this, select_state.class);
//                startActivity(i);
                finish();
            }
        });





        Intent intent = getIntent();

        String selectedJobCategory = intent.getStringExtra("selected_job_category");
        String selectedAgeCategory = intent.getStringExtra("selected_age_category");
        int budget = intent.getIntExtra("budget", -1);
        String stateName = intent.getStringExtra("state_name");

        // Toast the selected filters
        String filtersMessage = "Selected Job Category: " + selectedJobCategory + "\n" +
                "Selected Age Category: " + selectedAgeCategory + "\n" +
                "Budget of Travel: ₹" + budget;
        Toast.makeText(this, filtersMessage, Toast.LENGTH_SHORT).show();

        Cursor cursor = databaseHelper.getFilteredPlaceDetails(selectedAgeCategory, budget, stateName, selectedJobCategory);

        Log.d("data", String.valueOf(cursor.getCount()));
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                    @SuppressLint("Range") String placeName = cursor.getString(cursor.getColumnIndex("name"));
                    @SuppressLint("Range") String bestTime = cursor.getString(cursor.getColumnIndex("best_time"));
                    @SuppressLint("Range") String ageCategory = cursor.getString(cursor.getColumnIndex("age_category"));
                    @SuppressLint("Range") byte[] imageData = cursor.getBlob(cursor.getColumnIndex("image"));

                    placeDetailsList.add(placeName + "\n " + bestTime + "\n " + ageCategory + "\n" + id);
                    Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                    placeImages.add(imageBitmap);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        resultPageAdapter = new ResultPage_ADAPTER(placeDetailsList, placeImages);

        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(resultPageAdapter);
    }
}
