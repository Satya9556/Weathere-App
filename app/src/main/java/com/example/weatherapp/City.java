package com.example.weatherapp;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;

public class City {
    JSONObject values = new JSONObject();
    String city;
    public  Boolean setCity(String pincode , Context context){
        String url = "https://www.postpincode.in/api/getCityName.php?pincode="+pincode;
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        System.out.println(response);
                        try{
                            JSONArray arr = new JSONArray(response);

                            values = arr.getJSONObject(0);

                            city = values.getString("City");
                            System.out.println(city);
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
//        System.out.println(city);
        return city != null;
    }
    public String getCity(){
        System.out.println(city);
        return city;
    }
}
