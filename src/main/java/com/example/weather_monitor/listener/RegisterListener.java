package com.example.weather_monitor.listener;

import com.example.weather_monitor.WeatherMonitorController;
import com.example.weather_monitor.db.DBManager;
import com.example.weather_monitor.db.WeatherRecord;
import com.example.weather_monitor.db.db_dummy;
import com.example.weather_monitor.event.RegisterConfigEvent;
import com.google.common.eventbus.Subscribe;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/* Listener for register to manage changing its options */
public class RegisterListener {
    private static WeatherMonitorController weatherMonitorController;

    public RegisterListener(WeatherMonitorController weatherMonitorController) {
        this.weatherMonitorController = weatherMonitorController;
    }

    static db_dummy dummy = new db_dummy();
    private static final Thread recordingThread = new Thread(() -> {
        while (!Thread.currentThread().isInterrupted()) {
            printRecords();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    });
    private static int eventsHandled = 0;



    private static List<WeatherRecord> getRecords() {
        try(DBManager dbManager = new DBManager()) {
            return dbManager.getRecords();
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void printRecords() {
        List<WeatherRecord> records = getRecords();
        int startIndex = Math.max(records.size() - 10, 0);

        StringBuilder prompt = new StringBuilder();
        for (int i = startIndex; i < records.size(); i++) {
            prompt.append(records.get(i)).append("\n");
        }

        String result = prompt.toString();

        weatherMonitorController.setRegisterPromptText(result);
    }

    private static void startRecording() {
        recordingThread.start();
    }

    private static void stopRecording() {
        if (recordingThread.isAlive()) {
            recordingThread.interrupt();
            try {
                recordingThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void clearAllRecords() {
        try(DBManager dbManager = new DBManager()) {
            dbManager.clearRecords();
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
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
                clearAllRecords();
            }
        }
        stopRecording();
    }

    @Subscribe
    public void handleEvent(RegisterConfigEvent event) {
        System.out.println(this.getClass().getSimpleName() + " " + eventsHandled++ + ": " + event.toString());

        switch (event.option()) {
            case toggle_register -> {
                if(recordingThread.isAlive()) {
                    stopRecording();
                } else {
                    startRecording();
                }
            }
            case clear_register -> clearAllRecords();
            default -> throw new IllegalArgumentException("Wrong option: " + event.option());
        }
        // TODO: while recording will be updating the text field he have to do it with async CompletableFuture
    }
}
