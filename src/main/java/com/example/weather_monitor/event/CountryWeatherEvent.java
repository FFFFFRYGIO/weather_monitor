package com.example.weather_monitor.event;

import com.example.weather_monitor.db.Country;

/* Event to parse country weather recorder change */
public record CountryWeatherEvent(Country country, boolean toggle) {
    @Override
    public String toString() {
        return "CountryWeatherEvent{" +
                "country=" + country +
                ", toggle=" + toggle +
                '}';
    }
}
