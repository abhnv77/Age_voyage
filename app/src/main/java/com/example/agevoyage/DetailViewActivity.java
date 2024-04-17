package com.example.agevoyage;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DetailViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        Button ratingButton = findViewById(R.id.rating_button);
        ratingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailViewActivity.this,rating_activity.class);
                startActivity(intent);
            }
        });


        Intent intent = getIntent();

        // Get the ID from the Intent's extras
        int id = intent.getIntExtra("id", -1); // -1 is the default value if "id" is not found

        if (id != -1) {
            // Fetch place details from the database using the ID
            DatabaseHelper databaseHelper = new DatabaseHelper(this);
            Cursor cursor = databaseHelper.getPlaceById(id);

            if (cursor != null && cursor.moveToFirst()) {
                // Extract details from the cursor
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String bestTime = cursor.getString(cursor.getColumnIndex("best_time"));
                String ageCategory = cursor.getString(cursor.getColumnIndex("age_category"));
                String jobCategory = cursor.getString(cursor.getColumnIndex("job_category"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                String location = cursor.getString(cursor.getColumnIndex("location"));
                byte[] imageData = cursor.getBlob(cursor.getColumnIndex("image"));

                // Display the details in TextViews
                TextView nameTextView = findViewById(R.id.name_text_view);
                TextView locationTextView = findViewById(R.id.locationEditText);
                TextView bestTimeTextView = findViewById(R.id.best_time_text_view);
                TextView ageCategoryTextView = findViewById(R.id.age_category_text_view);
                TextView jobCategoryTextView = findViewById(R.id.job_category_text_view);
                TextView descriptionTextView = findViewById(R.id.description_text_view);

                nameTextView.setText(name);
                descriptionTextView.setText(description);
                bestTimeTextView.setText("Best Time to Visit: " + bestTime);
                ageCategoryTextView.setText("Age Category: " + ageCategory);
                jobCategoryTextView.setText("Best suitable for : " + jobCategory);

                // Convert byte array to Bitmap
                Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                // Display the image in an ImageView
                ImageView imageView = findViewById(R.id.image_view);
                imageView.setImageBitmap(imageBitmap);

                // Close the cursor after use
                cursor.close();

                // Set click listener for the location button
                View buttonShowLocation = findViewById(R.id.button_show_location);
                buttonShowLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Open the location URL in a web browser or the Google Maps app
                        openLocationUrl(location);
                    }
                });
            } else {
                // Handle case where cursor is null or empty
                Toast.makeText(this, "Place details not found", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Handle case where ID is not found in the intent extras
            Toast.makeText(this, "ID not found", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to open the location URL in a web browser or the Google Maps app
    private void openLocationUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
