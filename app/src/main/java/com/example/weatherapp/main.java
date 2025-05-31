package com.example.weatherapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.weatherapp.adapters.AllWeatherDetails;
import com.example.weatherapp.adapters.weekAdapter;
import com.example.weatherapp.adapters.weekModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class main extends AppCompatActivity implements LocationListener {
    Toolbar toolbar;
    TextView loc;
    LocationManager locationManager;
    AllWeatherDetails allWeatherDetails;
    weekAdapter adapter;
    String currentCity = "Not Found";;
    private static final String PERMISSION_LOCATION_COARSE = "android.permission.ACCESS_COARSE_LOCATION";
    private static final String PERMISSION_LOCATION_FINE = "android.permission.ACCESS_COARSE_LOCATION";
    private static final int PERMISSION_REQUEST_CODE = 200;
    ArrayList<weekModel> values = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        if (ContextCompat.checkSelfPermission(main.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(main.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
//        requestRuntimePermission();
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        locationFinder lf = new locationFinder();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationEnabled();
        getLocation();
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
    private void locationEnabled() {

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!gps_enabled && !network_enabled) {
            new AlertDialog.Builder(main.this)
                    .setTitle("Enable GPS Service")
                    .setMessage("We need your GPS location to show Near Places around you.")
                    .setCancelable(false)
                    .setPositiveButton("Enable", new
                            DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            })
                    .setNegativeButton("Cancel", (dialog , which) ->{
                        Toast.makeText(this, "Application needs location permission", Toast.LENGTH_SHORT).show();
                        this.finishAffinity();
                        dialog.dismiss();
                    } )
                    .show();
        }
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 5, (LocationListener) this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        String postalCode;
        try {

            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            System.out.println(addresses.get(0).getPostalCode());
            postalCode = addresses.get(0).getPostalCode();
            getCityName(postalCode);

        } catch (Exception e) {
        }
    }
    private void getCityName(String pincode){
        String url = "https://www.postpincode.in/api/getCityName.php?pincode="+pincode;
        final JSONObject[] values = {new JSONObject()};
        final String[] city = new String[1];
        RequestQueue queue = Volley.newRequestQueue(main.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray arr = new JSONArray(response);

                            values[0] = arr.getJSONObject(0);

                            city[0] = values[0].getString("City");
                            setValues(city[0]);
                            System.out.println(city[0]);
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }
    private void setValues(String city) {

        ArrayList<weekModel> datas = new ArrayList<>();
        String url = "https://api.weatherapi.com/v1/forecast.json?key=8f1079c2ca53422aa0a120005230907&q="+city+"&days=3&aqi=no&alerts=yes";
        RequestQueue queue = Volley.newRequestQueue(main.this);
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
                    System.out.println(objects[1].getJSONObject("day").getString("maxwind_kph"));

                    datas.add(new weekModel(objects[0].getString("temp_c") , "Today" , "Wednesday , 04/07/2023" , objects[0].getJSONObject("condition").getString("text") , city , objects[0].getString("humidity")+" %" ,"15%" , objects[0].getString("gust_kph")+" KM/H" ));
                    datas.add(new weekModel(objects[1].getJSONObject("day").getString("avgtemp_c") , "Tomorrow" ,"Wednesday , 04/07/2023" , objects[1].getJSONObject("day").getJSONObject("condition").getString("text") , city , objects[1].getJSONObject("day").getString("avghumidity")+" %" , objects[1].getJSONObject("day").getString("daily_chance_of_rain")+" %" ,objects[1].getJSONObject("day").getString("maxwind_kph")));
                    datas.add(new weekModel(objects[2].getJSONObject("day").getString("avgtemp_c") , "Tomorrow" ,"Wednesday , 04/07/2023" , objects[1].getJSONObject("day").getJSONObject("condition").getString("text") , city , objects[2].getJSONObject("day").getString("avghumidity")+" %" , objects[2].getJSONObject("day").getString("daily_chance_of_rain")+" %" ,objects[2].getJSONObject("day").getString("maxwind_kph")));
                    loc.setText(city);
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

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }
}
