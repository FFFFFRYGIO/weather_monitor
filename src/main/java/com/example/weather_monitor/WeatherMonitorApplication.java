package com.example.weather_monitor;

import com.example.weather_monitor.data.Country;
import com.example.weather_monitor.event.CountryRecordToggleEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WeatherMonitorApplication extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(WeatherMonitorApplication.class.getResource("weather_monitor-view.fxml"));
        AnchorPane rootPane = fxmlLoader.load();
        WeatherMonitorController controller = fxmlLoader.getController();
        controller.setRootPane(rootPane);

        Image icon = new Image(System.getProperty("user.dir") + "\\src\\main\\resources\\com\\example\\weather_monitor\\images\\icon.jpg");
        Scene scene = new Scene(rootPane, 1000, 492);
        stage.setTitle("Europe Weather Monitor");
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
    }

    public static void ThreadWorkingExample(CentralEventBus centralEventBus) {

        // Testing creating threads for counties
        CountryWeatherThread countryWeatherThread = new CountryWeatherThread(centralEventBus, Country.Poland, 500);
        CountryWeatherThread countryWeatherThread2 = new CountryWeatherThread(centralEventBus, Country.Germany, 700);
        countryWeatherThread.start();
        countryWeatherThread2.start();

        // Let the threads work for a while
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Stop threads
        countryWeatherThread.stopThread();
        countryWeatherThread2.stopThread();

        // Wait for threads to finish
        try {
            countryWeatherThread.join();
            countryWeatherThread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void CountryWeatherTogglingExample(CentralEventBus centralEventBus) {

        /*
        1. Start Poland
        2. Stop Poland
        3. Start Germany
        4. Start Poland
        5. Stop Germany
        6. Stop Poland
        */

        List<CountryRecordToggleEvent> countryRecordToggleEvents = new ArrayList<>();
        countryRecordToggleEvents.add(new CountryRecordToggleEvent(Country.Poland));
        countryRecordToggleEvents.add(new CountryRecordToggleEvent(Country.Poland));
        countryRecordToggleEvents.add(new CountryRecordToggleEvent(Country.Germany));
        countryRecordToggleEvents.add(new CountryRecordToggleEvent(Country.Poland));
        countryRecordToggleEvents.add(new CountryRecordToggleEvent(Country.Germany));
        countryRecordToggleEvents.add(new CountryRecordToggleEvent(Country.Poland));

        int x = 0;
        long startTime = System.currentTimeMillis();

        for(CountryRecordToggleEvent countryRecordToggleEvent : countryRecordToggleEvents) {

            System.out.println(x + "Beggin " + (System.currentTimeMillis() - startTime));

            centralEventBus.publishEventFromListener(countryRecordToggleEvent);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println(x++ + "End " + (System.currentTimeMillis() - startTime) + "\n\n");
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
