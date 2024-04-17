package com.example.agevoyage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ResultPage_ADAPTER extends RecyclerView.Adapter<ResultPage_ADAPTER.PlaceViewHolder> {

    private ArrayList<String> placeDetailsList;
    private ArrayList<Bitmap> placeImages;

    // Constructor to initialize both ArrayLists
    public ResultPage_ADAPTER(ArrayList<String> placeDetailsList, ArrayList<Bitmap> placeImages) {
        this.placeDetailsList = placeDetailsList;
        this.placeImages = placeImages;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_resultpage_layout, parent, false);
        return new PlaceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        String placeDetails = placeDetailsList.get(position); // Get the details for the current position
        Bitmap placeImage = placeImages.get(position); // Get the image for the current position
        holder.bind(placeDetails, placeImage, position); // Bind details and image to the ViewHolder
    }

    @Override
    public int getItemCount() {
        return placeDetailsList.size(); // Return the size of the placeDetailsList
    }

    public static class PlaceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textViewName;
        public TextView textViewDetails;
        public ImageView imageView;
        private int position;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewDetails = itemView.findViewById(R.id.text_view_details);
            imageView = itemView.findViewById(R.id.image_view); // Add ImageView

            // Set click listener
            itemView.setOnClickListener(this);
        }

        int id=0;

        public void bind(String placeDetails, Bitmap placeImage, int position) {
            this.position = position; // Store the position
            // Split the details into individual fields
            String[] detailsArray = placeDetails.split("\n");
            if (detailsArray.length >= 3) { // Ensure that there are at least 3 elements (name, best time, age category)
                String name = detailsArray[0];
                String bestTime = detailsArray[1];
                String ageCategory = detailsArray[2];
                id = Integer.parseInt(detailsArray[3]);
                // Set the details to TextViews
                textViewName.setText(name);
                textViewDetails.setText(Html.fromHtml("<b>Best Time to Visit: </b>" + bestTime + "<br>" + "Age Category: " + ageCategory));

                // Set the image to ImageView
                if (placeImage != null) {
                    imageView.setImageBitmap(placeImage);
                } else {
                    // Handle case where image data is null (no image available)
                    imageView.setImageResource(R.drawable.ka_state); // Set a default image placeholder
                }
            }
        }

        @Override
        public void onClick(View v) {
            // Handle item click
            Intent intent = new Intent(v.getContext(), DetailViewActivity.class);
            intent.putExtra("id", id); // Pass the position (or ID) to the next activity
            v.getContext().startActivity(intent);
        }
    }
}
