package com.tony.weatherapp;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tony.weatherapp.model.Day;

import java.text.DecimalFormat;
import java.util.List;

public class ForecastListAdapter extends RecyclerView.Adapter<ForecastListAdapter.ForecastListViewHolder> {

    private List<Day> data;
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    private static String TAG = "FORECAST_LIST_ADAPTER";



    public ForecastListAdapter(List<Day> data) {
        this.data = data;
    }

    static class ForecastListViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;
        TextView tempTextView;
        TextView descriptionTextView;
        TextView whatDayTextView;
        ImageView weatherIcon;

        Drawable cloudyDrawable;
        Drawable snowyDrawable;
        Drawable sunnyDrawable;
        Drawable rainyDrawable;




        ForecastListViewHolder(LinearLayout layout) {
            super(layout);
            this.layout = layout;
            tempTextView = layout.findViewById(R.id.dayTemp);
            descriptionTextView = layout.findViewById(R.id.dayDescription);
            whatDayTextView = layout.findViewById(R.id.whatDayTextView);
            weatherIcon = layout.findViewById(R.id.weatherIcon);

            cloudyDrawable = layout.getResources().getDrawable(R.drawable.cloudy);
            snowyDrawable = layout.getResources().getDrawable(R.drawable.snowy);
            sunnyDrawable = layout.getResources().getDrawable(R.drawable.sunny);
            rainyDrawable = layout.getResources().getDrawable(R.drawable.rain);

        }
    }

    @NonNull
    @Override
    public ForecastListAdapter.ForecastListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LinearLayout layout = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.forecast_list_element, parent, false);

        ForecastListViewHolder viewHolder = new ForecastListViewHolder(layout);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastListAdapter.ForecastListViewHolder holder, int position) {

        Day day = data.get(position);
        String temp = df2.format(day.getTemp());
        String whatDay = String.valueOf(day.getWhatDay());
        holder.whatDayTextView.setText(whatDay);
        holder.tempTextView.setText(temp + " f");
        holder.descriptionTextView.setText(day.getDescription());
        if (day.getDescription().contains("clear")) {
            Log.d(TAG, "It's clear");
            holder.weatherIcon.setImageDrawable(holder.sunnyDrawable);
        }
        else if (day.getDescription().contains("rain")) {
            Log.d(TAG, "It's rainy");
            holder.weatherIcon.setImageDrawable(holder.rainyDrawable);
        }else if (day.getDescription().contains("snow")) {
            Log.d(TAG, "It's snowy");
            holder.weatherIcon.setImageDrawable(holder.snowyDrawable);
        }else if (day.getDescription().contains("cloud")) {
            Log.d(TAG, "It's cloudy");
            holder.weatherIcon.setImageDrawable(holder.cloudyDrawable);
        }



    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
