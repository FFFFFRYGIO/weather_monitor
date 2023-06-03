package com.example.weather_monitor.event;

import com.example.weather_monitor.data.Country;
import com.example.weather_monitor.db.WeatherRecord;

/* Event to parse new weather register */
public record WeatherRecordEvent(WeatherRecord weatherRecord) {
    @Override
    public String toString() {
        return "WeatherRecordEvent{" +
                ", weatherRecord=" + weatherRecord +
                '}';
    }
}
