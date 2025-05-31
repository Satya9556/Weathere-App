package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.Objects;

public class SearchActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText cityName;
    Button currentLocation;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ListView list  = (ListView) findViewById(R.id.list_view);
        toolbar = findViewById(R.id.toolBar_temp);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        cityName = (EditText) toolbar.findViewById(R.id.enteredCity);
        currentLocation = (Button) findViewById(R.id.currentLoc);
        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SearchActivity.this , main.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
//                SearchActivity.this.finish();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_button , menu);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            super.onBackPressed();
        }
        if(item.getItemId() == R.id.search_button){
            if(!TextUtils.isEmpty(cityName.getText().toString())){
                Intent i = new Intent(SearchActivity.this , weather.class);
                i.putExtra("cityName" , cityName.getText().toString());
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);

            }
        }
        return super.onOptionsItemSelected(item);
    }
}