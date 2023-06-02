package com.example.weather_monitor;

import com.example.weather_monitor.data.Country;
import com.example.weather_monitor.event.WeatherRecordEvent;

/* Thread class that will gather the weather update from api and parse to the application */
public class CountryWeatherThread extends Thread {
    private final CentralEventBus centralEventBus;
    private final Country country;
    private final int period;
    private volatile boolean running = true;

    public CountryWeatherThread(CentralEventBus centralEventBus, Country country, int period) {
        this.centralEventBus = centralEventBus;
        this.country = country;
        this.period = period;
    }

    public WeatherRecordEvent generateEvent() {
        // get data from API
        // TODO: Get data from API (by parsing only country)
        Country country1 = country;
        float temperature1 = 11.11F;
        boolean isWindy1 = true;

        return new WeatherRecordEvent(country, temperature1, isWindy1);
    }

    public void publishEvent() {
        centralEventBus.publishEventFromListener(generateEvent());
    }

    @Override
    public void run() {
        while (running) {
            publishEvent();
            try {
                Thread.sleep(period);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopThread() {
        running = false;
    }

    public Country getCountry() {
        return country;
    }
}
