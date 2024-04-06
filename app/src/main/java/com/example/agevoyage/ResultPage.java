package com.example.agevoyage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;

import java.util.ArrayList;

public class ResultPage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseHelper databaseHelper;
    private ResultPage_ADAPTER resultPageAdapter; // Renamed from ResultPage_ADAPTER
    private ArrayList<String> placeDetailsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_page);

        recyclerView = findViewById(R.id.result_recycler_view);
        databaseHelper = new DatabaseHelper(this);
        placeDetailsList = new ArrayList<>(); // Initialize placeDetailsList

        Cursor cursor = databaseHelper.getAllPlaceDetails();

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String placeName = cursor.getString(cursor.getColumnIndex("name"));
                    @SuppressLint("Range") String bestTime = cursor.getString(cursor.getColumnIndex("best_time"));
                    @SuppressLint("Range") String ageCategory = cursor.getString(cursor.getColumnIndex("age_category"));
                    @SuppressLint("Range") String imageUri = cursor.getString(cursor.getColumnIndex("image_uri"));

                    placeDetailsList.add(placeName + "\n " + bestTime + "\n " + ageCategory + "\nImage URI: " + imageUri);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        resultPageAdapter = new ResultPage_ADAPTER(placeDetailsList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(resultPageAdapter);
    }
}
