package com.example.weather_monitor.event;

import com.example.weather_monitor.data.RegisterOption;

/* Event to parse register options changes */
public record RegisterConfigEvent(RegisterOption option) {

    @Override
    public String toString() {
        return "RegisterConfigEvent{" +
                "option=" + option +
                '}';
    }
}
