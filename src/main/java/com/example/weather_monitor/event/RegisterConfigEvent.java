package com.example.weather_monitor.event;


import com.example.weather_monitor.db.RegisterOption;

/* Event to parse register options changes */
public record RegisterConfigEvent(RegisterOption option, boolean toggle) {

    @Override
    public String toString() {
        return "RegisterConfigEvent{" +
                "option=" + option +
                ", toggle=" + toggle +
                '}';
    }
}
