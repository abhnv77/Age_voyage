package com.example.agevoyage;

import android.content.Intent;
import android.os.Bundle;
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
    private Button btnApplyFilters;
    private TextView budgetAmountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_selection);

        // Initialize views
        spinnerJobCategory = findViewById(R.id.spinner_job_category);
        spinnerAgeCategory = findViewById(R.id.spinner_age_category);
        seekBarBudget = findViewById(R.id.seekBar_budget);
        btnApplyFilters = findViewById(R.id.button_apply_filters);
        budgetAmountTextView = findViewById(R.id.textView_budget_amount);

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
        // This method will be called when the "Apply Filters" button is clicked
        updateToastMessage(); // Update the toast message to reflect the latest budget
        Intent intent = new Intent(Category_Selection.this, ResultPage.class);

        // Start the ResultPage activity
        startActivity(intent);
    }

    private void updateBudgetAmount(int amount) {
        budgetAmountTextView.setText("Budget: ₹" + amount);
    }

    private void updateToastMessage() {
        // Get selected job category, age category, and budget
        String selectedJobCategory = spinnerJobCategory.getSelectedItem().toString();
        String selectedAgeCategory = spinnerAgeCategory.getSelectedItem().toString();
        int budget = seekBarBudget.getProgress();

        // Show toast message with selected filters and budget
        String message = "Selected Job Category: " + selectedJobCategory + "\n" +
                "Selected Age Category: " + selectedAgeCategory + "\n" +
                "Budget of Travel: ₹" + budget;
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
