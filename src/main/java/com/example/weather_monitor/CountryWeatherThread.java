package com.example.weather_monitor;

public class CountryWeatherThread {
    private final CentralEventBus centralEventBus;

    public CountryWeatherThread(CentralEventBus centralEventBus) {
        this.centralEventBus = centralEventBus;
    }

    public void publishEvent(Object event) {
        centralEventBus.publishEventFromListener(event);
    }
}
