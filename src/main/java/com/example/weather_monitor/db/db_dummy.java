package com.example.weather_monitor.db;

import java.util.ArrayList;
import java.util.List;

public class db_dummy {
    static List<WeatherRecord> weatherRecords = new ArrayList<WeatherRecord>();

    public void addRecord(WeatherRecord weatherRecord) {
        weatherRecords.add(weatherRecord);
    }

    public static List<WeatherRecord> getRecords() {
        return weatherRecords;
    }

    public void clearRecords() {
        weatherRecords.clear();
    }
}
