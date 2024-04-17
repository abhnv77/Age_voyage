package com.example.agevoyage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Base64;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class LoginAdmim_Add extends AppCompatActivity {

    // Declare UI elements
    private EditText editTextPlaceName, descriptionEditText,locationEditText;
    private Spinner spinnerBestTime, spinnerAgeCategory, spinnerSelectState, spinnerJobCategories; // Added spinnerJobCategories
    private Button buttonSave, buttonSelectImage;
    private SeekBar seekBarBudget;
    private String imageUriString;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admim_add);

        // Initialize UI elements
        editTextPlaceName = findViewById(R.id.editTextPlaceName);
        spinnerBestTime = findViewById(R.id.spinnerBestTime);
        spinnerAgeCategory = findViewById(R.id.spinnerAgeCategory);
        spinnerSelectState = findViewById(R.id.spinnerSelectState);
        spinnerJobCategories = findViewById(R.id.spinnerJobCategories); // Initialize spinner for job categories
        buttonSave = findViewById(R.id.buttonSave);
        buttonSelectImage = findViewById(R.id.buttonSelectImage);
        seekBarBudget = findViewById(R.id.seekBar_budget);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        locationEditText= findViewById(R.id.locationEditText);

        // Create instance of DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Populate spinners
        populateSpinners();

        // Set seek bar max
        seekBarBudget.setMax(50000);

        // Set seek bar change listener
        seekBarBudget.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView budgetAmountTextView = findViewById(R.id.budget_amount);
                budgetAmountTextView.setText("Selected Amount: ₹" + (progress + 5000));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Handle save button click
        buttonSave.setOnClickListener(v -> savePlaceDetails());

        // Handle select image button click
        buttonSelectImage.setOnClickListener(v -> selectImage());
    }

    private void populateSpinners() {
        // Populate spinner with best times to visit
        ArrayAdapter<String> bestTimesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.best_times_to_visit));
        bestTimesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBestTime.setAdapter(bestTimesAdapter);

        // Populate spinner with age categories
        ArrayAdapter<String> ageCategoriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.age_categories));
        ageCategoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAgeCategory.setAdapter(ageCategoriesAdapter);

        // Populate spinner with states
        ArrayAdapter<String> selectStateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.spinnerSelectState));
        selectStateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSelectState.setAdapter(selectStateAdapter);

        // Populate spinner with job categories
        ArrayAdapter<String> jobCategoriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.job_categories_array));
        jobCategoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJobCategories.setAdapter(jobCategoriesAdapter);
    }

    private void savePlaceDetails() {
        // Retrieve input values
        String placeName = editTextPlaceName.getText().toString();
        String bestTime = spinnerBestTime.getSelectedItem().toString();
        String ageCategory = spinnerAgeCategory.getSelectedItem().toString();
        String state = spinnerSelectState.getSelectedItem().toString();
        String location = locationEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        int budgetAmount = seekBarBudget.getProgress() + 5000;
        String jobCategory = spinnerJobCategories.getSelectedItem().toString(); // Retrieve selected job category

        // Check if any field is empty
        if (placeName.isEmpty() || bestTime.isEmpty() || ageCategory.isEmpty() || state.isEmpty()) {
            showToast("Please fill in all fields.");
            return;
        }

        // Insert place details into the database
        if (imageUriString == null || imageUriString.isEmpty()) {
            showToast("Please select an image.");
            return;
        }

        byte[] imageBytes = Base64.decode(imageUriString, Base64.DEFAULT);
        boolean isInserted = databaseHelper.insertPlaceData(placeName, bestTime, ageCategory, state, location, imageBytes, budgetAmount, description, jobCategory);

        if (isInserted) {
            showToast("Place details saved successfully!");
            Intent intent = new Intent(LoginAdmim_Add.this, login_admin_view.class);
            startActivity(intent);
            finish();
        } else {
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
            Uri imageUri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, len);
                }
                byte[] imageBytes = byteArrayOutputStream.toByteArray();
                imageUriString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                TextView selectedImageTextView = findViewById(R.id.selected_image);
                selectedImageTextView.setText("Selected Image: " + getFileName(imageUri));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("Range")
    private String getFileName(Uri uri) {
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
