package com.example.weather_monitor.listener;

import com.google.common.eventbus.Subscribe;

public class CountryThreadsListener {
    private static int eventsHandled = 0;

    @Subscribe
    public void stringEvent(String event) {
        eventsHandled++;
        System.out.println("CountryThreadsListener: " + eventsHandled);
    }

}
