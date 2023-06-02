package com.example.weather_monitor;

import com.google.common.eventbus.EventBus;

public class CentralEventBus {
    private final EventBus eventBus = new EventBus();

    public void registerNewListener(Object listener) {
        eventBus.register(listener);
    }

    public void publishEventFromListener(Object event) {
        eventBus.post(event);
    }
}
