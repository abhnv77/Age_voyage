package com.example.agevoyage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class PlaceCursorAdapter extends CursorAdapter {

    private DatabaseHelper databaseHelper;

    public PlaceCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        databaseHelper = new DatabaseHelper(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_place_details, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {


        // Find views in the item layout
        TextView textViewPlaceName = view.findViewById(R.id.textViewPlaceName);
        TextView textViewDetails = view.findViewById(R.id.textViewDetails);
        Button buttonDelete = view.findViewById(R.id.buttonDelete);
        Button buttonEdit = view.findViewById(R.id.buttonEdit);


        // Extract place details from the cursor
        final int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        final String placeName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        String bestTime = cursor.getString(cursor.getColumnIndexOrThrow("best_time"));
        String ageCategory = cursor.getString(cursor.getColumnIndexOrThrow("age_category"));

        // Set place details to views
        textViewPlaceName.setText(placeName);
        textViewDetails.setText("Best Time: " + bestTime + ", Age Category: " + ageCategory);

        // Handle delete button click
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean deletionSuccessful =databaseHelper.deletePlace(placeName);
                if (deletionSuccessful) {
                    // Remove item from the dataset and notify adapter hi

                    notifyDataSetChanged();

                    // Display a toast message
                    Toast.makeText(context.getApplicationContext(), "Item deleted", Toast.LENGTH_SHORT).show();

                    // Reload the activity
                    Intent intent = new Intent(context, login_admin_view.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                } else {
                    Toast.makeText(context, "Failed to delete item", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start EditActivity with corresponding data and ID
                Intent intent = new Intent(context, EditActivity.class);
                intent.putExtra("id", id);

                context.startActivity(intent);
            }
        });
    }
}
