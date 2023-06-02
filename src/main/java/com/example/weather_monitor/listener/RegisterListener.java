package com.example.weather_monitor.listener;

import com.example.weather_monitor.event.RegisterConfigEvent;
import com.google.common.eventbus.Subscribe;

/* Listener for register to manage changing its options */
public class RegisterListener {
    private static int eventsHandled = 0;

    @Subscribe
    public void handleEvent(RegisterConfigEvent event) {
        System.out.println(this.getClass().getSimpleName() + " " + eventsHandled++ + ": " + event.toString());
    }
}
