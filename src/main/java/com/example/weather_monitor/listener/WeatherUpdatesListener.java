package com.example.weather_monitor.listener;

import com.example.weather_monitor.event.WeatherEvent;
import com.google.common.eventbus.Subscribe;

public class WeatherUpdatesListener {
    private static int eventsHandled = 0;

    @Subscribe
    public void stringEvent(WeatherEvent event) {
        eventsHandled++;
        System.out.println("WeatherUpdatesListener new event number " + eventsHandled);
        System.out.println("Event: " + event.toString());
    }


}

