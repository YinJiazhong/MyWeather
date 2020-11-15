package com.example.myweather.dao;

import com.example.myweather.entity.CityWeather;

import java.util.HashMap;
import java.util.Map;

public class CityWeatherRepository {

    private Map<String, CityWeather> cityWeatherMap = new HashMap();

    private CityWeatherRepository(){

    }

    private static CityWeatherRepository cityWeatherRepository = new CityWeatherRepository();

    public static CityWeatherRepository getInstance(){
        return cityWeatherRepository;
    }

    public Map<String, CityWeather> getCityWeatherMap() {
        return cityWeatherMap;
    }

    public void setCityWeatherMap(Map<String, CityWeather> cityWeatherMap) {
        this.cityWeatherMap = cityWeatherMap;
    }
}
