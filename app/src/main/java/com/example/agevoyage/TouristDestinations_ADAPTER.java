package com.example.agevoyage;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TouristDestinations_ADAPTER extends RecyclerView.Adapter<TouristDestinations_ADAPTER.ViewHolder> {

    Context mycontext;
    ArrayList<TouristDestinations_MODEL> model;

    public TouristDestinations_ADAPTER(Context mycontext, ArrayList<TouristDestinations_MODEL> model) {
        this.mycontext = mycontext;
        this.model = model;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myview = LayoutInflater.from(mycontext).inflate(R.layout.home_recyclerview_layout,null, true);
        return new ViewHolder(myview);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        TouristDestinations_MODEL model2 = model.get(position);

        holder.image.setImageResource(model2.getImage());
        holder.name.setText(model2.name);
        holder.state.setText(model2.state);
    }

    @Override
    public int getItemCount() {
        return model.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name, state;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.city_image);
            name = itemView.findViewById(R.id.city_name);
            state = itemView.findViewById(R.id.state_name);

        }
    }
}

