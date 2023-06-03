package com.example.weather_monitor.listener;

import com.example.weather_monitor.CentralEventBus;
import com.example.weather_monitor.CountryWeatherThread;
import com.example.weather_monitor.event.CountryRecordToggleEvent;
import com.google.common.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/* Listener for recording updates to start/stop threads for country weather recorders */
public class CountryThreadsManageListener {
    private static int eventsHandled = 0;
    List<CountryWeatherThread> workingCountryWeatherThreads = new ArrayList<CountryWeatherThread>();
    private final CentralEventBus centralEventBus;
    private final int generalPeriod;

    public CountryThreadsManageListener(CentralEventBus centralEventBus, int generalPeriod) {
        this.centralEventBus = centralEventBus;
        this.generalPeriod = generalPeriod;
    }

    private void stopCountryWeatherThread(CountryWeatherThread countryWeatherThread) {

        // Stop thread
        countryWeatherThread.stopThread();

        // Wait for countryWeatherThread to finish
        try {
            countryWeatherThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Remove countryWeatherThread from list of workingCountryWeatherThreads
        workingCountryWeatherThreads.remove(countryWeatherThread);
    }

    private void startCountryWeatherThread(CountryRecordToggleEvent event) {

        // Create and start thread
        CountryWeatherThread countryWeatherThread = new CountryWeatherThread(centralEventBus, event.country(), generalPeriod);
        countryWeatherThread.start();

        // Add countryWeatherThread to list of workingCountryWeatherThreads
        workingCountryWeatherThreads.add(countryWeatherThread);
    }

    @Subscribe
    public void handleEvent(CountryRecordToggleEvent event) {
        System.out.println(this.getClass().getSimpleName() + " " + eventsHandled++ + ": " + event.toString());
        for(CountryWeatherThread countryWeatherThread : workingCountryWeatherThreads) {
            if(countryWeatherThread.getCountry() == event.country()){
                //Thread found in list of workingCountryWeatherThreads, stopping
                stopCountryWeatherThread(countryWeatherThread);
                // Event fully handled
                return;
            }
        }

        // Country not in the threads list, starting new Thread
        startCountryWeatherThread(event);
    }
}
