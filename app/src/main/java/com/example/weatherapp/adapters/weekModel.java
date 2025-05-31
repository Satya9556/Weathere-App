package com.example.weatherapp.adapters;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class weekModel implements Parcelable {
    public String temperature;
    public String day;
    public String dayDate;
    public String weather;
    public String city;
    public String humidity;
    public String rain;
    public String wind;
    public weekModel(String temperature , String day , String dayDate , String weather , String city
            , String humidity , String rain , String wind){
        this.temperature = temperature;
        this.day = day;
        this.dayDate = dayDate;
        this.weather = weather;
        this.city = city;
        this.humidity = humidity;
        this.rain = rain;
        this.wind = wind;
    }

    protected weekModel(Parcel in) {
        temperature = in.readString();
        day = in.readString();
        dayDate = in.readString();
        weather = in.readString();
        city = in.readString();
        humidity = in.readString();
        rain = in.readString();
        wind = in.readString();
    }

    public static final Creator<weekModel> CREATOR = new Creator<weekModel>() {
        @Override
        public weekModel createFromParcel(Parcel in) {
            return new weekModel(in);
        }

        @Override
        public weekModel[] newArray(int size) {
            return new weekModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(temperature);
        parcel.writeString(day);
        parcel.writeString(dayDate);
        parcel.writeString(weather);
        parcel.writeString(city);
        parcel.writeString(humidity);
        parcel.writeString(rain);
        parcel.writeString(wind);
    }
    //    public void setDay(String day) {
//        this.day = day;
//    }
//
//    public void setDayDate(String dayDate) {
//        this.dayDate = dayDate;
//    }
//
//    public void setWeather(String weather) {
//        this.weather = weather;
//    }
//
//    public String getTemperature() {
//        return temperature;
//    }
//
//    public String getDay() {
//        return day;
//    }
//
//    public String getDayDate() {
//        return dayDate;
//    }
//
//    public String getWeather() {
//        return weather;
//    }
}
