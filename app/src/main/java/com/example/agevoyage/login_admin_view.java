package com.example.agevoyage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class login_admin_view extends AppCompatActivity {

    private ListView listViewPlaces;
    private ArrayList<String> placeDetailsList;
    private DatabaseHelper databaseHelper;
    private PlaceCursorAdapter adapter;
    private Button backButton;
    private Button loginPageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin_view);

        listViewPlaces = findViewById(R.id.listViewPlaces);

        placeDetailsList = new ArrayList<>();
        databaseHelper = new DatabaseHelper(this);

        Cursor cursor = databaseHelper.getAllPlaceDetails();
        adapter = new PlaceCursorAdapter(this, cursor);

        listViewPlaces.setAdapter(adapter);
        // Populate the list view with place details from the database
        displayPlaceDetails();

        backButton = findViewById(R.id.buttonBack);
        loginPageButton = findViewById(R.id.buttonLoginPage);

        // Set click listener for the back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to LoginAdminAdd activity
                Intent intent = new Intent(login_admin_view.this, LoginAdmim_Add.class);
                startActivity(intent);
            }
        });

        // Set click listener for the login page button
        loginPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to LoginPage activity
                Intent intent = new Intent(login_admin_view.this, loginPage.class);
                startActivity(intent);
            }
        });
    }

    private void displayPlaceDetails() {
        Cursor cursor = databaseHelper.getAllPlaceDetails();
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String placeName = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String bestTime = cursor.getString(cursor.getColumnIndex("best_time"));
                @SuppressLint("Range") String ageCategory = cursor.getString(cursor.getColumnIndex("age_category"));
                @SuppressLint("Range") String imageUri = cursor.getString(cursor.getColumnIndex("image_uri"));
                placeDetailsList.add(placeName + "\nBest Time: " + bestTime + "\nAge Category: " + ageCategory + "\nImage URI: " + imageUri);
            } while (cursor.moveToNext());
        }
        cursor.close();

        listViewPlaces.setAdapter(adapter);
    }

    private void deleteItemFromDatabase(String placeName) {
        // Implement your logic here to delete the item with the given place name from the database
        // Example:
        // databaseHelper.deletePlace(placeName);
    }

    private class CustomAdapter extends ArrayAdapter<String> {
        CustomAdapter(ArrayList<String> data) {
            super(login_admin_view.this, R.layout.list_item_place_details, data);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_place_details, parent, false);
            }

            String currentItem = getItem(position);
            if (currentItem != null) {
                String[] parts = currentItem.split("\n");
                String placeName = parts[0];
                String details = parts[1] + "\n" + parts[2] + "\n" + parts[3];
                Toast.makeText(getContext(), "this is a test", Toast.LENGTH_SHORT).show();

                TextView textViewPlaceName = itemView.findViewById(R.id.textViewPlaceName);
                TextView textViewDetails = itemView.findViewById(R.id.textViewDetails);

                textViewPlaceName.setText(placeName);
                textViewDetails.setText(details);

                // Handle delete button click delete evda
                Button deleteButton = itemView.findViewById(R.id.buttonDelete);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Call deletePlace method from DatabaseHelper
//                        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
                        boolean deletionSuccessful =databaseHelper.deletePlace(placeName);
                        if (deletionSuccessful) {
                            // Remove item from the dataset and notify adapter hi
                            remove(getItem(position));
                            notifyDataSetChanged();

                            // Display a toast message
                            Toast.makeText(getContext(), "Item deleted", Toast.LENGTH_SHORT).show();

                            // Reload the activity
                            Intent intent = new Intent(getContext(), login_admin_view.class);
                            getContext().startActivity(intent);
                            ((Activity) getContext()).finish(); // Finish the current activity
                        } else {
                            Toast.makeText(getContext(), "Failed to delete item", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            return itemView;
        }
    }
}
