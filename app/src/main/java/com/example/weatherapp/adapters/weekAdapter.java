package com.example.weatherapp.adapters;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;
import com.example.weatherapp.temperature;

import java.util.ArrayList;

public class weekAdapter extends RecyclerView.Adapter<weekAdapter.viewHolder> {
    Context context;
    ArrayList<weekModel> values;
    public void swap(ArrayList<weekModel> data){
        values.clear();
        values.addAll(data);
        notifyDataSetChanged();
    }
    public weekAdapter(Context context, ArrayList<weekModel> value){
        this.context = context;
        this.values = value;
    }
    @NonNull
    @Override
    public  viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recycler_weeks , parent  ,false);
        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.day.setText(values.get(position).day);
        holder.dayDate.setText(values.get(position).dayDate);
        holder.weather.setText(values.get(position).weather);
        holder.temperature.setText(values.get(position).temperature);
        weekModel val = values.get(position);
//        System.out.println(val.weather);
        holder.view_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, temperature.class);
                i.putExtra("data" , val);
                context.startActivity(i);
            }

        });
    }


    @Override
    public int getItemCount() {
        return values.size();
    }
    public static class viewHolder extends RecyclerView.ViewHolder{
        TextView day , dayDate , weather , temperature , view_button;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.day_showing);
            dayDate = itemView.findViewById(R.id.day_date_view);
            weather = itemView.findViewById(R.id.weather_view);
            temperature = itemView.findViewById(R.id.temp_view);
            view_button = itemView.findViewById(R.id.view_button);
        }
    }
}
