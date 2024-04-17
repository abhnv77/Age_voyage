package com.example.agevoyage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Category_Selection extends AppCompatActivity {

    private Spinner spinnerJobCategory, spinnerAgeCategory;
    private SeekBar seekBarBudget;
    private Button btnApplyFilters,selectstate;
    private TextView budgetAmountTextView;

    private String state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_selection);


        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("state_name")) {
            String stateName = intent.getStringExtra("state_name");
            if (stateName != null && !stateName.isEmpty()) {
                // Show the state name in the toast message
                Toast.makeText(getApplicationContext(), "Selected State: " + stateName, Toast.LENGTH_SHORT).show();
            } else {
                // Handle the case where the state name is empty or null
                Toast.makeText(getApplicationContext(), "Invalid State Name", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Handle the case where the state name is not provided
            Toast.makeText(getApplicationContext(), "State name not provided", Toast.LENGTH_SHORT).show();
        }
        // Initialize views
        spinnerJobCategory = findViewById(R.id.spinner_job_category);
        spinnerAgeCategory = findViewById(R.id.spinner_age_category);
        seekBarBudget = findViewById(R.id.seekBar_budget);
        btnApplyFilters = findViewById(R.id.button_apply_filters);
        budgetAmountTextView = findViewById(R.id.textView_budget_amount);
        selectstate = findViewById(R.id.selectstate);

        selectstate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1= new Intent(Category_Selection.this,select_state.class);
                startActivity(intent1);
            }
        });

        // Set up spinners
        ArrayAdapter<CharSequence> jobAdapter = ArrayAdapter.createFromResource(this,
                R.array.job_categories_array, android.R.layout.simple_spinner_item);
        jobAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJobCategory.setAdapter(jobAdapter);

        ArrayAdapter<CharSequence> ageAdapter = ArrayAdapter.createFromResource(this,
                R.array.age_categories_array, android.R.layout.simple_spinner_item);
        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAgeCategory.setAdapter(ageAdapter);

        // Set up SeekBar for budget
        seekBarBudget.setMax(50000); // Maximum budget
        seekBarBudget.setProgress(1000); // Default budget
        seekBarBudget.setMin(5000);
        updateBudgetAmount(seekBarBudget.getProgress());


        // Apply Filters button click listener
        btnApplyFilters.setOnClickListener(v -> applyFilters());

        // SeekBar change listener to update budget while dragging
        seekBarBudget.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateBudgetAmount(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void applyFilters() {
        // Get selected job category, age category, and budget
        String selectedJobCategory = spinnerJobCategory.getSelectedItem().toString();
        String selectedAgeCategory = spinnerAgeCategory.getSelectedItem().toString();
        int budget = seekBarBudget.getProgress();

        // Get the state name from the intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("state_name")) {
            String stateName = intent.getStringExtra("state_name");

            // Create intent for the next activity
            Intent nextIntent = new Intent(Category_Selection.this, ResultPage.class);

            // Pass filters and state name as extras to the next activity
            nextIntent.putExtra("selected_job_category", selectedJobCategory);
            nextIntent.putExtra("selected_age_category", selectedAgeCategory);
            nextIntent.putExtra("budget", budget);
            nextIntent.putExtra("state_name", stateName);

            // Start the ResultPage activity with the intent containing all required data
            startActivity(nextIntent);
        } else {
            // Handle the case where the state name is not provided
            Toast.makeText(getApplicationContext(), "State name not provided", Toast.LENGTH_SHORT).show();
        }
    }


    private void updateBudgetAmount(int amount) {
        budgetAmountTextView.setText("Budget: ₹" + amount);
    }

//    private void updateToastMessage() {
//        // Get selected job category, age category, and budget
//        String selectedJobCategory = spinnerJobCategory.getSelectedItem().toString();
//        String selectedAgeCategory = spinnerAgeCategory.getSelectedItem().toString();
//        int budget = seekBarBudget.getProgress();
//
//        // Show toast message with selected filters and budget
//        String message = "Selected Job Category: " + selectedJobCategory + "\n" +
//                "Selected Age Category: " + selectedAgeCategory + "\n" +
//                "Budget of Travel: ₹" + budget;
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//    }
}
