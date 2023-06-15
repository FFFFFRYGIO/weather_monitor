package com.example.weather_monitor.listener;

import com.example.weather_monitor.APIWeatherManager;
import com.example.weather_monitor.WeatherMonitorController;
import com.example.weather_monitor.db.DBManager;
import com.example.weather_monitor.db.WeatherRecord;
import com.example.weather_monitor.event.RegisterConfigEvent;
import com.google.common.eventbus.Subscribe;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/* Listener for register to manage changing its options */
public class RegisterListener {
    private static WeatherMonitorController weatherMonitorController;
    private static int maxRowsInRegister;

    public RegisterListener(WeatherMonitorController weatherMonitorController) {
        this.weatherMonitorController = weatherMonitorController;

        final Properties properties = new Properties();
        String propFileName = "config.properties";
        InputStream inputStream = APIWeatherManager.class.getClassLoader().getResourceAsStream(propFileName);

        try {
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        maxRowsInRegister = Integer.parseInt(properties.getProperty("max_rows_in_register"));

    }
    private static Thread recordingThread;
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

        if(records.size() == 0) {
            weatherMonitorController.setRegisterPromptText("No records");
            return;
        }

        int startIndex = Math.max(records.size() - maxRowsInRegister, 0);


        StringBuilder stringBuilder = new StringBuilder();
        for (int i = startIndex; i < records.size(); i++) {
            stringBuilder.append(records.get(i).toString()).append("\n");
        }

        String prompt = stringBuilder.toString();

        weatherMonitorController.setRegisterPromptText(prompt);
    }

    private static void startRecording() {
        recordingThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                printRecords();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
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
        weatherMonitorController.setRegisterPromptText("No records");
    }

    @Subscribe
    public void handleEvent(RegisterConfigEvent event) {
        System.out.println(this.getClass().getSimpleName() + " " + eventsHandled++ + ": " + event.toString());

        switch (event.option()) {
            case toggle_register -> {
                if(recordingThread != null && recordingThread.isAlive()) {
                    stopRecording();
                } else {
                    startRecording();
                }
            }
            case clear_register -> clearAllRecords();
            default -> throw new IllegalArgumentException("Wrong option: " + event.option());
        }
    }
}
