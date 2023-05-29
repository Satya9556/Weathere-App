package com.example.weatherapp;

import java.text.DecimalFormat;

public class weatherStructure {
    private static final DecimalFormat decfor = new DecimalFormat("0.0");
    public String weatherMain , weatherDescrription , weatherImageId , mainTemp , mainTempMin , mainTempMax , mainPressure , mainHumidity , visibility , windSpeed , windDeg;
    public weatherStructure(String weatherMain , String weatherDescrription , String weatherImageId , String mainTemp , String mainTempMin , String mainTempMax ,
                            String  mainPressure , String mainHumidity , String visibility , String windSpeed , String windDeg){
        this.weatherMain = weatherMain;
        this.weatherDescrription = weatherDescrription;
        this.weatherImageId = weatherImageId;
        this.mainTemp = decfor.format(Double.parseDouble(mainTemp) - 273);
        this.mainTempMin = decfor.format(Double.parseDouble(mainTempMin) - 273);
        this.mainTempMax = decfor.format(Double.parseDouble(mainTempMax) - 273);
        this.mainPressure = mainPressure;
        this.mainHumidity = mainHumidity;
        this.visibility = Double.toString(Double.parseDouble(visibility) / 1000);
        this.windSpeed = windSpeed;
        this.windDeg = windDeg;
    }
}
