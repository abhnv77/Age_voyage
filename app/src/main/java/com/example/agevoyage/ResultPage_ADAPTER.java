package com.example.agevoyage;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.text.Html;
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

    private ArrayList<String> placeDetailsList; // Update to ArrayList<String>

    public ResultPage_ADAPTER(ArrayList<String> placeDetailsList) {
        this.placeDetailsList = placeDetailsList;
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
        holder.bind(placeDetails); // Bind details to the ViewHolder
    }

    @Override
    public int getItemCount() {
        return placeDetailsList.size(); // Return the size of the placeDetailsList
    }



    public static class PlaceViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public TextView textViewDetails;
        public ImageView imageView;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewDetails = itemView.findViewById(R.id.text_view_details);
            imageView = itemView.findViewById(R.id.image_view); // Add ImageView
        }

        public void bind(String placeDetails) {
            // Split the details into individual fields
            String[] detailsArray = placeDetails.split("\n");
            if (detailsArray.length >= 4) { // Ensure that there are at least 4 elements
                String name = detailsArray[0];
                String bestTime = detailsArray[1];
                String ageCategory = detailsArray[2];
                String imageUri = detailsArray[3];

                // Set the details to TextViews
                textViewName.setText(name);
                textViewDetails.setText(Html.fromHtml("<b>Best Time to Visit: </b>" + bestTime + "<br>" + "Age Category: " + ageCategory + "<br><br>" + "Image URI: " + imageUri));

                // Load image using Picasso
                Picasso.get().load(imageUri).into(imageView);

                // Access context through itemView
                Toast.makeText(itemView.getContext(), "test test", Toast.LENGTH_SHORT).show();
            }
        }

    }

}

