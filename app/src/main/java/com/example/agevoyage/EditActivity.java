package com.example.agevoyage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class EditActivity extends AppCompatActivity {
    private EditText editTextPlaceName, descriptionEditText,locationEditText;
    private Spinner spinnerSelectState,spinnerJobCategories;
    private Spinner spinnerBestTime;
    private Spinner spinnerAgeCategory;
    private Button buttonSave;
    private SeekBar seekBarBudget;
    private String imageUriString;
    private Button buttonSelectImage;
    private DatabaseHelper databaseHelper;
    private int placeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Initialize views
        editTextPlaceName = findViewById(R.id.editTextPlaceName);
        spinnerSelectState = findViewById(R.id.spinnerSelectState);
        spinnerBestTime = findViewById(R.id.spinnerBestTime);
        spinnerAgeCategory = findViewById(R.id.spinnerAgeCategory);
        spinnerJobCategories = findViewById(R.id.spinnerJobCategories);
        buttonSave = findViewById(R.id.buttonSave);
        seekBarBudget = findViewById(R.id.seekBar_budget);
        buttonSelectImage = findViewById(R.id.buttonSelectImage);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        locationEditText= findViewById(R.id.locationEditText);  //new

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Get ID from intent
        placeId = getIntent().getIntExtra("id", -1);

        // Populate spinners
        populateSpinners();

        // Populate EditText fields with existing data
        if (placeId != -1) {
            populateFields();
        } else {
            Toast.makeText(this, "Invalid place ID", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity
        }

        // Save changes button click listener
        buttonSave.setOnClickListener(view -> {
            // Update data in the database
            updatePlaceData();
        });

        buttonSelectImage.setOnClickListener(v -> selectImage());
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

            try {
                // Convert the selected image to a byte array
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, len);
                }
                byte[] imageBytes = byteArrayOutputStream.toByteArray();
                imageUriString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                // Set the selected image file name
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


    private void populateSpinners() {
        // You need to populate your spinners with appropriate data
        // For example:
        ArrayAdapter<CharSequence> stateAdapter = ArrayAdapter.createFromResource(this,
                R.array.spinnerSelectState, android.R.layout.simple_spinner_item);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSelectState.setAdapter(stateAdapter);


        ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter.createFromResource(this,
                R.array.best_times_to_visit, android.R.layout.simple_spinner_item);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBestTime.setAdapter(timeAdapter);

        ArrayAdapter<CharSequence> ageAdapter = ArrayAdapter.createFromResource(this,
                R.array.age_categories_array, android.R.layout.simple_spinner_item);
        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAgeCategory.setAdapter(ageAdapter);

        ArrayAdapter<CharSequence> jobAdapter = ArrayAdapter.createFromResource(this,
                R.array.job_categories_array, android.R.layout.simple_spinner_item);
        jobAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJobCategories.setAdapter(jobAdapter);


    }

    private void populateFields() {
        Cursor cursor = databaseHelper.getPlaceById(placeId);
        if (cursor != null && cursor.moveToFirst()) {
            String placeName = cursor.getString(cursor.getColumnIndex("name"));
            String state = cursor.getString(cursor.getColumnIndex("state"));
            String location = cursor.getString(cursor.getColumnIndex("location"));
            String bestTime = cursor.getString(cursor.getColumnIndex("best_time"));
            String ageCategory = cursor.getString(cursor.getColumnIndex("age_category"));
            String description = cursor.getString(cursor.getColumnIndex("description"));
            int budgetAmount = cursor.getInt(cursor.getColumnIndex("budgetamount")); // Use getInt for budget amount
            String jobCategory = cursor.getString(cursor.getColumnIndex("job_category"));

            // Set the fetched data to the EditText fields and spinners
            editTextPlaceName.setText(placeName);
            descriptionEditText.setText(description);
            locationEditText.setText(location);

            int jobCategoryPosition = getJobCategoryPosition(jobCategory);
            if (jobCategoryPosition != -1) {
                spinnerJobCategories.setSelection(jobCategoryPosition);
            }

            // Find the position of the existing data in the spinner and set it
            int statePosition = getStatePosition(state);
            if (statePosition != -1)
                spinnerSelectState.setSelection(statePosition);

            int bestTimePosition = getBestTimePosition(bestTime);
            if (bestTimePosition != -1)
                spinnerBestTime.setSelection(bestTimePosition);

            int ageCategoryPosition = getAgeCategoryPosition(ageCategory);
            if (ageCategoryPosition != -1)
                spinnerAgeCategory.setSelection(ageCategoryPosition);

            seekBarBudget.setProgress(budgetAmount - 5000); // No need for parseInt since it's already an int
            TextView budgetAmountTextView = findViewById(R.id.budget_amount);
            budgetAmountTextView.setText("Selected Amount: ₹" + budgetAmount);

            // Set SeekBar listener to update budget amount
            seekBarBudget.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    // Update the displayed budget amount as the SeekBar is adjusted
                    budgetAmountTextView.setText("Selected Amount: ₹" + (progress + 5000));
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

            cursor.close();
        } else {
            Toast.makeText(this, "Place not found", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity
        }
    }


    private int getStatePosition(String state) {
        // Find the position of the state in the spinner data
        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinnerSelectState.getAdapter();
        return adapter.getPosition(state);
    }

    private int getBestTimePosition(String bestTime) {
        // Find the position of the best time in the spinner data
        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinnerBestTime.getAdapter();
        return adapter.getPosition(bestTime);
    }

    private int getAgeCategoryPosition(String ageCategory) {
        // Find the position of the age category in the spinner data
        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinnerAgeCategory.getAdapter();
        return adapter.getPosition(ageCategory);
    }

    private int getBudgetAmount() {
        // Get the SeekBar's progress and add 5000 to it to get the actual budget amount
        return seekBarBudget.getProgress() + 5000;
    }
    private int getJobCategoryPosition(String jobCategory) {
        // Find the position of the job category in the spinner data
        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinnerJobCategories.getAdapter();
        return adapter.getPosition(jobCategory);
    }

    private void updatePlaceData() {
        // Get the updated data from the views
        String placeName = editTextPlaceName.getText().toString();
        String bestTime = spinnerBestTime.getSelectedItem().toString();
        String ageCategory = spinnerAgeCategory.getSelectedItem().toString();
        String state = spinnerSelectState.getSelectedItem().toString();
        String jobCategory = spinnerJobCategories.getSelectedItem().toString();
        String location = locationEditText.getText().toString(); // Get the location data from the appropriate view or input field
        String description = descriptionEditText.getText().toString();

        int budgetAmount = getBudgetAmount(); // Updated to use the method to get the budget amount



        if (imageUriString == null || imageUriString.isEmpty()) {
            showToast("Please select an image.");
            return;
        }

        // Insert the place details into the database
        byte[] imageBytes = Base64.decode(imageUriString, Base64.DEFAULT);

        boolean isUpdated = databaseHelper.updatePlaceData(placeId, placeName, bestTime, ageCategory, state, location, imageBytes, budgetAmount, description, jobCategory);
        if (isUpdated) {
            Toast.makeText(this, "Place data updated successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(EditActivity.this, login_admin_view.class);
            finish(); // Close the activity
            startActivity(intent);
        } else {
            Toast.makeText(this, "Failed to update place data", Toast.LENGTH_SHORT).show();
        }
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
