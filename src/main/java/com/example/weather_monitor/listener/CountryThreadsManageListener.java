package com.example.weather_monitor.listener;

import com.example.weather_monitor.event.RecordToggleEvent;
import com.example.weather_monitor.event.WeatherEvent;
import com.google.common.eventbus.Subscribe;

/* Listener for recording updates to start/stop threads for country weather recorders */
public class CountryThreadsManageListener {
    private static int eventsHandled = 0;

    @Subscribe
    public void handleEvent(RecordToggleEvent event) {
        System.out.println(this.getClass().getSimpleName() + " " + eventsHandled++ + ": " + event.toString());
    }
}
