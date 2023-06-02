package com.example.weather_monitor.listener;

import com.google.common.eventbus.Subscribe;

/* Listener for recording updates to start/stop threads for country weather recorders */
public class CountryThreadsListener {
    private static int eventsHandled = 0;

    @Subscribe
    public void handleEvent(CountryThreadsListener event) {
        System.out.println("RegisterListener new event number " + eventsHandled++);
        System.out.println("Event: " + event.toString());
    }
}
