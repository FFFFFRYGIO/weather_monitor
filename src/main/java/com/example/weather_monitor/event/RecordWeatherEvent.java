package com.example.weather_monitor.event;


import com.example.weather_monitor.db.Country;

public record RecordWeatherEvent(Country country) {

    @Override
    public String toString() {
        return "RecordWeatherEvent{" +
                "country=" + country +
                '}';
    }
}
