package com.example.weather_monitor;

import com.example.weather_monitor.db.Country;
import com.example.weather_monitor.event.WeatherEvent;

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

    public void publishEvent() {
        WeatherEvent event = new WeatherEvent(country, 11.11F, true);
        centralEventBus.publishEventFromListener(event);
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
}
