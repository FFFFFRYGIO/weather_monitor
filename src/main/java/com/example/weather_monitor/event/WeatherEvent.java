package com.example.weather_monitor.event;

import com.example.weather_monitor.db.Country;

/* Event to parse new weather register */
public record WeatherEvent(Country country, float temperature, boolean isWindy) {
    @Override
    public String toString() {
        return "RegisterWeatherEvent{" +
                "country=" + country +
                ", temperature=" + temperature +
                ", isWindy=" + isWindy +
                '}';
    }
}
