package com.example.weather_monitor.event;


public class WeatherEvent {
    private float temperature;
    private boolean isWindy;

    public WeatherEvent(float temperature, boolean isWindy) {
        this.temperature = temperature;
        this.isWindy = isWindy;
    }

    public float getTemperature() {
        return temperature;
    }

    public boolean isWindy() {
        return isWindy;
    }


    @Override
    public String toString() {
        return "WeatherEvent{" +
                "temperature=" + temperature +
                ", isWindy=" + isWindy +
                '}';
    }
}
