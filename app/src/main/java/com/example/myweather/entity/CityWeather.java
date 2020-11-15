package com.example.myweather.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class CityWeather {

    private String responseInfo;
    private String cityName;
    private int currentTemp;
    private int maxTemp;
    private int minTemp;
    private int feelLikeTemp;
    private String iconName;
    private long timestamp;

    public CityWeather(String responseInfo) throws JSONException {
        this.responseInfo = responseInfo;
        JSONObject jsonObject = new JSONObject(responseInfo);
        JSONArray weather = jsonObject.getJSONArray("weather");
        iconName = "img_"  + ((JSONObject)weather.get(0)).getString("icon");

        cityName = jsonObject.getString("name");
        JSONObject mainJSONObject = jsonObject.getJSONObject("main");
        mainJSONObject.getInt("temp");
        feelLikeTemp = mainJSONObject.getInt("feels_like");
        currentTemp = mainJSONObject.getInt("temp");
        maxTemp= mainJSONObject.getInt("temp_max");
        minTemp = mainJSONObject.getInt("temp_min");
        this.timestamp = System.currentTimeMillis();
    }

    public String getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(String responseInfo) {
        this.responseInfo = responseInfo;
    }

    public int getCurrentTemp() {
        return currentTemp;
    }

    public void setCurrentTemp(int currentTemp) {
        this.currentTemp = currentTemp;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(int maxTemp) {
        this.maxTemp = maxTemp;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(int minTemp) {
        this.minTemp = minTemp;
    }

    public int getFeelLikeTemp() {
        return feelLikeTemp;
    }

    public void setFeelLikeTemp(int feelLikeTemp) {
        this.feelLikeTemp = feelLikeTemp;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public String getCityName() {
        return cityName;
    }

    public boolean isOverHour(){
        if(System.currentTimeMillis() - this.timestamp > 1000 * 60 * 60){
            return true;
        }
        return false;
    }
}
