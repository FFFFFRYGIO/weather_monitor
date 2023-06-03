package com.example.weather_monitor.listener;

import com.example.weather_monitor.db.Connector;
import com.example.weather_monitor.db.WeatherRecord;
import com.example.weather_monitor.db.db_dummy;
import com.example.weather_monitor.event.RegisterConfigEvent;
import com.google.common.eventbus.Subscribe;

import java.io.IOException;
import java.sql.*;
import java.util.List;

import static com.example.weather_monitor.data.RegisterOption.*;

/* Listener for register to manage changing its options */
public class RegisterListener {
    static db_dummy dummy = new db_dummy();
    private static Thread recordingThread;
    private static int eventsHandled = 0;

    private static List<WeatherRecord> getRecords() {
        return dummy.getRecords();
    }

    private static void printRecords() {
        List<WeatherRecord> records = getRecords();
        int startIndex = Math.max(records.size() - 10, 0);

        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Records:");
        for (int i = startIndex; i < records.size(); i++) {
            WeatherRecord record = records.get(i);
            System.out.println("\t" + record);
        }
    }

    private static void startRecording() {
        recordingThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                printRecords();
                try {
                    Thread.sleep(1000); // Wait for 1 second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        recordingThread.start();
    }

    private static void stopRecording() {
        if (recordingThread != null && recordingThread.isAlive()) {
            recordingThread.interrupt();
            try {
                recordingThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void clearAllRecordings() {
        dummy.clearRecords();
    }

    public static void main(String[] args) {
        // Sample testing if everything works
        startRecording();

        for(int i=0; i<100; i++) {
            WeatherRecord weatherRecord = new WeatherRecord();
            weatherRecord.setId((long) i);
            dummy.addRecord(weatherRecord);
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if(i%30 == 0) {
                clearAllRecordings();
            }
        }
        // stopRecording();
    }

    @Subscribe
    public void handleEvent(RegisterConfigEvent event) {
        System.out.println(this.getClass().getSimpleName() + " " + eventsHandled++ + ": " + event.toString());

        switch (event.option()) {
            case start_register -> startRecording();
            case stop_register -> stopRecording();
            case clear_register -> clearAllRecordings();
            default -> throw new IllegalArgumentException("Wrong option: " + event.option());
        }
        // TODO: start is running thread that gathers rows from database and prints them
        // TODO: stop is stopping updating
        // TODO: clear is clearing the viewed output
        // TODO: optimize updating: print only new rows and maybe do scrolling (hide oldest, print new)
        // TODO: be careful while clearing: it needs also clear the structure resposible for printing
    }
}
