package com.example.weather_monitor.event;


public record WeatherEvent(float temperature, boolean isWindy) {
    @Override
    public String toString() {
        return "WeatherEvent{" +
                "temperature=" + temperature +
                ", isWindy=" + isWindy +
                '}';
    }
}
