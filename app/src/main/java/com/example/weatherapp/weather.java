package com.example.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.weatherapp.adapters.weekAdapter;
import com.example.weatherapp.adapters.weekModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class weather extends AppCompatActivity {
    Toolbar toolbar;
    TextView loc;
    LocationManager locationManager;
    weekAdapter adapter;
    ArrayList<weekModel> values = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Intent i = getIntent();
        setValues(i.getStringExtra("cityName"));
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        locationFinder lf = new locationFinder();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        loc = (TextView) toolbar.findViewById(R.id.location_nav_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new weekAdapter(this , values);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_option , menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.search_btn){
            Intent i = new Intent(getApplicationContext(), SearchActivity.class);
//            i.putExtra("cityName" , "london");
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setValues(String city) {

        ArrayList<weekModel> datas = new ArrayList<>();
        String url = "https://api.weatherapi.com/v1/forecast.json?key=8f1079c2ca53422aa0a120005230907&q="+city+"&days=3&aqi=no&alerts=yes";
        RequestQueue queue = Volley.newRequestQueue(weather.this);
        JSONObject[] objects = new JSONObject[3];
        final JSONObject[] forecat = {new JSONObject()};
        JSONArray[] forecastDay = new JSONArray[1];
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println("hello");
                    objects[0] = response.getJSONObject("current");
                    forecat[0] = response.getJSONObject("forecast");
                    forecastDay[0] = forecat[0].getJSONArray("forecastday");
                    objects[1] = forecastDay[0].getJSONObject(1);
                    objects[2] = forecastDay[0].getJSONObject(2);

                    datas.add(new weekModel(objects[0].getString("temp_c") , "Today" , "Wednesday , 04/07/2023" , objects[0].getJSONObject("condition").getString("text") , response.getJSONObject("location").getString("name") , objects[0].getString("humidity")+" %" ,"15%" , objects[0].getString("gust_kph")+" KM/H" ));
                    datas.add(new weekModel(objects[1].getJSONObject("day").getString("avgtemp_c") , "Tomorrow" ,"Wednesday , 04/07/2023" , objects[1].getJSONObject("day").getJSONObject("condition").getString("text") , response.getJSONObject("location").getString("name") , objects[1].getJSONObject("day").getString("avghumidity")+" %" , objects[1].getJSONObject("day").getString("daily_chance_of_rain")+" %" ,objects[1].getJSONObject("day").getString("maxwind_kph")+" KM/H"));
                    datas.add(new weekModel(objects[2].getJSONObject("day").getString("avgtemp_c") , "Tomorrow" ,"Wednesday , 04/07/2023" , objects[1].getJSONObject("day").getJSONObject("condition").getString("text") , response.getJSONObject("location").getString("name") , objects[2].getJSONObject("day").getString("avghumidity")+" %" , objects[2].getJSONObject("day").getString("daily_chance_of_rain")+" %" ,objects[2].getJSONObject("day").getString("maxwind_kph")+" KM/H"));
                    loc.setText(response.getJSONObject("location").getString("name"));
                    adapter.swap(datas);

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }
}
