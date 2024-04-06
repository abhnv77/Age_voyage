package com.example.agevoyage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginAdmim_Add extends AppCompatActivity {

    private EditText editTextPlaceName,descriptionEditText;
    private Spinner spinnerBestTime, spinnerAgeCategory, spinnerSelectState;
    private Button buttonSave, buttonSelectImage;
    private SeekBar seekBarBudget;
    private String imageUriString; // To store the URI of the selected image
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admim_add);

        editTextPlaceName = findViewById(R.id.editTextPlaceName);
        spinnerBestTime = findViewById(R.id.spinnerBestTime);
        spinnerAgeCategory = findViewById(R.id.spinnerAgeCategory);
        spinnerSelectState = findViewById(R.id.spinnerSelectState);
        buttonSave = findViewById(R.id.buttonSave);
        buttonSelectImage = findViewById(R.id.buttonSelectImage);
        seekBarBudget = findViewById(R.id.seekBar_budget);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        // Create instance of DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Populate spinner with best times to visit
        String[] bestTimes = getResources().getStringArray(R.array.best_times_to_visit);
        ArrayAdapter<String> bestTimesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bestTimes);
        bestTimesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBestTime.setAdapter(bestTimesAdapter);

        // Populate spinner with age categories
        String[] ageCategories = getResources().getStringArray(R.array.age_categories);
        ArrayAdapter<String> ageCategoriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ageCategories);
        ageCategoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAgeCategory.setAdapter(ageCategoriesAdapter);

        // Populate spinner with states
        String[] selectstate = getResources().getStringArray(R.array.spinnerSelectState);
        ArrayAdapter<String> selectstateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, selectstate);
        selectstateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSelectState.setAdapter(selectstateAdapter);

        // Set a seek bar change listener
        seekBarBudget.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Display the selected budget amount
                TextView budgetAmountTextView = findViewById(R.id.budget_amount);
                budgetAmountTextView.setText("Selected Amount: ₹" + (progress + 5000)); // Assuming the SeekBar range is from 5000 to 50000+
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Not needed for this implementation
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Not needed for this implementation
            }
        });
        seekBarBudget.setMax(50000);

        // Handle save button click
        buttonSave.setOnClickListener(v -> savePlaceDetails());

        // Handle select image button click
        buttonSelectImage.setOnClickListener(v -> selectImage());
    }

    private void savePlaceDetails() {
        String placeName = editTextPlaceName.getText().toString();
        String bestTime = spinnerBestTime.getSelectedItem().toString();
        String ageCategory = spinnerAgeCategory.getSelectedItem().toString();
        String state = spinnerSelectState.getSelectedItem().toString(); // Get selected state
        String description = descriptionEditText.getText().toString(); // Initialize description itha putya coloumn

        // Get the selected budget amount from the SeekBar
        int budgetAmount = seekBarBudget.getProgress() + 5000; // Assuming the SeekBar range is from 5000 to 50000+

        // Check if any field is empty
        if (placeName.isEmpty() || bestTime.isEmpty() || ageCategory.isEmpty() || state.isEmpty()) {
            showToast("Please fill in all fields.");
            // Change color of unfilled fields
            if (placeName.isEmpty()) {
                editTextPlaceName.setHintTextColor(getResources().getColor(android.R.color.holo_red_light));
            }
            if (bestTime.isEmpty()) {
                ((TextView)spinnerBestTime.getSelectedView()).setTextColor(getResources().getColor(android.R.color.holo_red_light));
            }
            if (ageCategory.isEmpty()) {
                ((TextView)spinnerAgeCategory.getSelectedView()).setTextColor(getResources().getColor(android.R.color.holo_red_light));
            }
            if (state.isEmpty()) {
                ((TextView)spinnerSelectState.getSelectedView()).setTextColor(getResources().getColor(android.R.color.holo_red_light));
            }
            return; // Exit the method
        }

        // Check if description is provided
        if (!description.isEmpty()) {
            description = descriptionEditText.getText().toString();
        }



        // Insert the place details into the database
        boolean isInserted = databaseHelper.insertPlaceData(placeName, bestTime, ageCategory, state, imageUriString, budgetAmount, description);

        if (isInserted) {
            // If insertion is successful, show a success message
            showToast("Place details saved successfully!");

            // Redirect to login_admin_view.java
            Intent intent = new Intent(LoginAdmim_Add.this, login_admin_view.class);
            startActivity(intent);
            finish();
        } else {
            // If insertion fails, show an error message
            showToast("Failed to save place details.");
        }
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the URI of the selected image
            Uri imageUri = data.getData();
            imageUriString = imageUri.toString(); // Store the image URI as a string

            // Set the selected image file name
            TextView selectedImageTextView = findViewById(R.id.selected_image);
            selectedImageTextView.setText("Selected Image: " + getFileName(imageUri));
        }
    }

    @SuppressLint("Range")
    private String getFileName(Uri uri) { // retrieves the file name from a URI
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
