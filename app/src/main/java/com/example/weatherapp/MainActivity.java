package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    private final String apiKey = "42145950cac26a8e633c3e93565bbc60";
    private String url;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    private ProgressBar spinner;
    private String valueSaved;
    EditText city;
    ImageButton search;
    TextView text , temperature , cityName , description , pressure , humid , visible , speed;
    ImageView weatherImage;
    weatherStructure values;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        city = findViewById(R.id.city);
        search = findViewById(R.id.search);
        text = findViewById(R.id.details);
        temperature = findViewById(R.id.temperature);
        cityName = findViewById(R.id.cityName);
        description = findViewById(R.id.descriptionOfWeather);
        weatherImage = findViewById(R.id.weatherImage);
        pressure = findViewById(R.id.pressure);
        humid = findViewById(R.id.humid);
        visible = findViewById(R.id.visible);
        speed = findViewById(R.id.speed);
        spinner = (ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(city.getText().toString())){
                    Toast.makeText(MainActivity.this, "Field is Empty", Toast.LENGTH_SHORT).show();
                }else{
                    spinner.setVisibility(View.VISIBLE);
                    setData(city.getText().toString());
                }
            }
        });
        loadData();
        if(valueSaved != null){
            setData(valueSaved);
        }

    }
    private void setData(String city){
        url = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+apiKey+"";
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        JSONObject[] object = new JSONObject[3];
        JSONArray[] weatherDetails = new JSONArray[1];
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(JSONObject response) {
                try {
                    weatherDetails[0] = response.getJSONArray("weather");
                    object[0] = weatherDetails[0].getJSONObject(0);
                    object[1] = response.getJSONObject("main");
                    object[2] = response.getJSONObject("wind");
                    values = new weatherStructure(object[0].getString("main") , object[0].getString("description") , object[0].getString("icon") ,
                            object[1].getString("temp") , object[1].getString("temp_min") , object[1].getString("temp_max") ,
                            object[1].getString("pressure") , object[1].getString("humidity") , response.getString("visibility") ,
                            object[2].getString("speed") , object[2].getString("deg"));
                    temperature.setText(values.mainTemp+" \u2103");
                    cityName.setText(city);
                    Picasso.get()
                            .load("https://openweathermap.org/img/wn/"+values.weatherImageId+"@2x.png")
                            .into(weatherImage);
                    description.setText(values.weatherDescrription);
                    pressure.setText(values.mainPressure+" hPa");
                    humid.setText(values.mainHumidity+" %");
                    visible.setText(values.visibility+" Km");
                    speed.setText(values.windSpeed+ " m/s");
                    spinner.setVisibility(View.GONE);
                    saveData(city);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    spinner.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "The connection timed out", Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    //TODO
                    spinner.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Authetication failure", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    //TODO
                    spinner.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Unable to connect to the server", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    //TODO
                    spinner.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Unable to connect to internet", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    //TODO
                    spinner.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "There was a problem parsing the data", Toast.LENGTH_SHORT).show();
                }
            }
        });
        queue.add(request);
    }
    public void saveData(String city){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(TEXT, city);
        editor.apply();
    }
    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        valueSaved = sharedPreferences.getString(TEXT, "");
    }
}