package com.example.weather_monitor.listener;

import com.example.weather_monitor.event.RecordWeatherEvent;
import com.google.common.eventbus.Subscribe;

public class WeatherUpdatesListener {
    private static int eventsHandled = 0;

    @Subscribe
    public void handleEvent(RecordWeatherEvent event) {
        System.out.println("WeatherUpdatesListener new event number " + eventsHandled++);
        System.out.println("Event: " + event.toString());
    }
}

