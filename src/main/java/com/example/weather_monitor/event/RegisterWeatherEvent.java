package com.example.weather_monitor.event;

/* Event to parse new weather register */
public record RegisterWeatherEvent(float temperature, boolean isWindy) {
    @Override
    public String toString() {
        return "RegisterEvent{" +
                "temperature=" + temperature +
                ", isWindy=" + isWindy +
                '}';
    }
}
