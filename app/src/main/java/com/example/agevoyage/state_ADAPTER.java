package com.example.agevoyage;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class state_ADAPTER extends RecyclerView.Adapter<state_ADAPTER.StateItemViewHolder> {

    private OnStateClickListener onStateClickListener;

    private Context context;
    private ArrayList<state_MODEL> stateList;

    public state_ADAPTER(Context context, ArrayList<state_MODEL> stateList) {
        this.context = context;
        this.stateList = stateList;
    }

    @NonNull
    @Override
    public StateItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_recyclerview_layout, parent, false);
        return new StateItemViewHolder(view);
    }

    public interface OnStateClickListener {
        void onStateClick(state_MODEL state);
    }

    public void setOnStateClickListener(OnStateClickListener listener) {
        this.onStateClickListener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull StateItemViewHolder holder, int position) {
        state_MODEL model = stateList.get(position);

        holder.image.setImageResource(model.getImage());
        holder.name.setText(model.getName());
        holder.state.setText(model.getState());
    }

    @Override
    public int getItemCount() {
        return stateList.size();
    }

    public class StateItemViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name, state;

        public StateItemViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.city_image);
            name = itemView.findViewById(R.id.city_name);
            state = itemView.findViewById(R.id.state_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION && onStateClickListener != null) {
                        Log.d("pos",String.valueOf(stateList.get(position).getName()));
                        onStateClickListener.onStateClick(stateList.get(position));
                    }
                }
            });
        }
    }
}
