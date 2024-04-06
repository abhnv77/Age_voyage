package com.example.agevoyage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import java.util.ArrayList;

    public class select_state extends AppCompatActivity implements state_ADAPTER.OnStateClickListener {

    RecyclerView statesRecyclerView;
    ArrayList<state_MODEL> stateModels;
    state_ADAPTER stateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_state);

        // Initialize RecyclerView
        statesRecyclerView = findViewById(R.id.touristdestinations_recyclerview);

        // Set up GridLayoutManager with 2 span count
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        statesRecyclerView.setLayoutManager(layoutManager);

        // Populate RecyclerView with state data
        stateModels = new ArrayList<>();

        stateModels.add(new state_MODEL(R.drawable.kerala_state, "","kerala"));
        stateModels.add(new state_MODEL(R.drawable.agra_taj_mahal, "","Delhi"));
        stateModels.add(new state_MODEL(R.drawable.tn_state, "","Tamil Nadu"));
        stateModels.add(new state_MODEL(R.drawable.rj_state, "","Rajasthan"));
        stateModels.add(new state_MODEL(R.drawable.ka_state, "","Karnataka"));
        stateModels.add(new state_MODEL(R.drawable.pj_state, "","Punjab"));
        stateModels.add(new state_MODEL(R.drawable.goa_state, "","Goa"));
        stateModels.add(new state_MODEL(R.drawable.hp_state, "","Himachal Pradesh"));
        stateModels.add(new state_MODEL(R.drawable.jk_state, "","Jammu Kashmir"));
        stateModels.add(new state_MODEL(R.drawable.tl_state, "","Telengana"));

        // Add more states as needed

        // Initialize and set adapter
        stateAdapter = new state_ADAPTER(this, stateModels);
        statesRecyclerView.setAdapter(stateAdapter);

        // Set click listener to the adapter
        stateAdapter.setOnStateClickListener(this);

    }

    @Override
    public void onStateClick(state_MODEL state) {
        // Handle the click event for the state item
        // For example, you can start a new activity to display details about the state
        Intent intent = new Intent(this, Category_Selection.class);
        intent.putExtra("state_name", state.getState());
        startActivity(intent);
    }
}
