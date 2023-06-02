package com.example.weather_monitor.listener;

import com.example.weather_monitor.event.RegisterConfigEvent;
import com.google.common.eventbus.Subscribe;

/* Listener for register to manage changing its options */
public class RegisterListener {
    private static int eventsHandled = 0;

    @Subscribe
    public void handleEvent(RegisterConfigEvent event) {
        System.out.println(this.getClass().getSimpleName() + " " + eventsHandled++ + ": " + event.toString());
        // TODO: start is running thread that gathers rows from database and prints them
        // TODO: stop is stopping updating
        // TODO: clear is clearing the viewed output
        // TODO: optimize updating: print only new rows and maybe do scrolling (hide oldest, print new)
        // TODO: be careful while clearing: it needs also clear the structure resposible for printing
    }
}
