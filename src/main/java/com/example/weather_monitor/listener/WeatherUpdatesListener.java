package com.example.weather_monitor.listener;

import com.example.weather_monitor.event.RegisterConfigEvent;
import com.google.common.eventbus.Subscribe;

/* Listener for weather registers parsed to save them in database and update it on the map */
public class WeatherUpdatesListener {
    private static int eventsHandled = 0;

    @Subscribe
    public void handleEvent(RegisterConfigEvent event) {
        System.out.println("WeatherUpdatesListener new event number " + eventsHandled++);
        System.out.println("Event: " + event.toString());
    }
}

