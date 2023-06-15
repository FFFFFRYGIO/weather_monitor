package com.example.weather_monitor.listener;

import com.example.weather_monitor.WeatherMonitorController;
import com.example.weather_monitor.db.DBManager;
import com.example.weather_monitor.event.WeatherRecordEvent;
import com.google.common.eventbus.Subscribe;

import java.io.IOException;
import java.sql.SQLException;

/* Listener for weather registers parsed to save them in database and update it on the map */
public class WeatherUpdatesListener {
    private static int eventsHandled = 0;
    private static WeatherMonitorController weatherMonitorController;

    public WeatherUpdatesListener(WeatherMonitorController weatherMonitorController) {
        this.weatherMonitorController = weatherMonitorController;
    }

    @Subscribe
    public void handleEvent(WeatherRecordEvent event) {
        System.out.println(this.getClass().getSimpleName() + " " + eventsHandled++ + ": " + event.toString());
        try (DBManager dbManager = new DBManager()) {
            dbManager.addRecord(event.weatherRecord());
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
        weatherMonitorController.setWeatherPromptData(event.weatherRecord());
    }
}
