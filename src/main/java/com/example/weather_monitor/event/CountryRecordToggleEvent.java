package com.example.weather_monitor.event;

import com.example.weather_monitor.data.Country;

/* Event to parse country weather recorder change */
public record CountryRecordToggleEvent(Country country) {
    @Override
    public String toString() {
        return "CountryWeatherEvent{" +
                "country=" + country +
                '}';
    }
}
