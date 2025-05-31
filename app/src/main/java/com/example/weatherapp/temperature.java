package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.weatherapp.adapters.weekModel;

import java.util.Objects;

public class temperature extends AppCompatActivity {
    Toolbar toolbar_temp;
    TextView temp , weather , date , humidity, rain , wind;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);
        toolbar_temp = findViewById(R.id.toolBar_temp);
        setSupportActionBar(toolbar_temp);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        TextView location = (TextView) toolbar_temp.findViewById(R.id.location_temp_nav);
        Intent intent = getIntent();
        weekModel values = (weekModel) intent.getParcelableExtra("data");
        temp = (TextView) findViewById(R.id.temp_view);
        weather = (TextView) findViewById(R.id.weather_view);
        date = (TextView) findViewById(R.id.day_date);
        humidity = (TextView) findViewById(R.id.humidity_value);
        rain = (TextView) findViewById(R.id.rain_value);
        wind = (TextView) findViewById(R.id.wind_value);
        if(values != null){
            temp.setText(values.temperature);
            weather.setText(values.weather);
            date.setText(values.dayDate);
            location.setText(values.city);
            humidity.setText(values.humidity);
            rain.setText(values.rain);
            wind.setText(values.wind);
        }



    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}