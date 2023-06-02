package com.example.weather_monitor.listener;

import com.example.weather_monitor.event.WeatherRecordEvent;
import com.google.common.eventbus.Subscribe;

/* Listener for weather registers parsed to save them in database and update it on the map */
public class WeatherUpdatesListener {
    private static int eventsHandled = 0;

    @Subscribe
    public void handleEvent(WeatherRecordEvent event) {
        System.out.println(this.getClass().getSimpleName() + " " + eventsHandled++ + ": " + event.toString());
        // TODO: add record to the database
    }
}
