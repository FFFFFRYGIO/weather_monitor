package com.example.weather_monitor.event;

import com.example.weather_monitor.data.Country;

/* Event to parse new weather register */
public record WeatherRecordEvent(Country country, float temperature, boolean isWindy) {
    @Override
    public String toString() {
        return "RegisterWeatherEvent{" +
                "country=" + country +
                ", temperature=" + temperature +
                ", isWindy=" + isWindy +
                '}';
    }
}
