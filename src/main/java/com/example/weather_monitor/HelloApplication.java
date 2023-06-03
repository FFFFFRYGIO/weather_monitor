package com.example.weather_monitor;

import com.example.weather_monitor.data.Country;
import com.example.weather_monitor.event.RecordToggleEvent;
import com.example.weather_monitor.listener.CountryThreadsManageListener;
import com.example.weather_monitor.listener.RegisterListener;
import com.example.weather_monitor.listener.WeatherUpdatesListener;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Image icon = new Image(System.getProperty("user.dir") + "\\src\\images\\icon.jpg");
        Scene scene = new Scene(fxmlLoader.load(), 1000, 1000);
        stage.setTitle("Hello!");
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

        List<RecordToggleEvent> recordToggleEvents = new ArrayList<RecordToggleEvent>();
        recordToggleEvents.add(new RecordToggleEvent(Country.Poland));
        recordToggleEvents.add(new RecordToggleEvent(Country.Poland));
        recordToggleEvents.add(new RecordToggleEvent(Country.Germany));
        recordToggleEvents.add(new RecordToggleEvent(Country.Poland));
        recordToggleEvents.add(new RecordToggleEvent(Country.Germany));
        recordToggleEvents.add(new RecordToggleEvent(Country.Poland));

        int x = 0;
        long startTime = System.currentTimeMillis();

        for(RecordToggleEvent recordToggleEvent : recordToggleEvents) {

            System.out.println(x + "Beggin " + (System.currentTimeMillis() - startTime));

            centralEventBus.publishEventFromListener(recordToggleEvent);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println(x++ + "End " + (System.currentTimeMillis() - startTime) + "\n\n");
        }
    }

    public static void main(String[] args) {
        // launch();

        // CentralEventBus
        CentralEventBus centralEventBus = new CentralEventBus();

        // CountryThreadsListener
        int generalPeriod = 250;
        CountryThreadsManageListener countryThreadsManageListener = new CountryThreadsManageListener(centralEventBus, generalPeriod);
        centralEventBus.registerNewListener(countryThreadsManageListener);

        // WeatherUpdatesListener
        WeatherUpdatesListener weatherUpdatesListener = new WeatherUpdatesListener();
        centralEventBus.registerNewListener(weatherUpdatesListener);

        // RegisterListener
        RegisterListener registerListener = new RegisterListener();
        centralEventBus.registerNewListener(registerListener);

        // Simple steps that shows how threads works
        // ThreadWorkingExample(centralEventBus);

        // Simple steps that shows how starting/stopping threads works
        CountryWeatherTogglingExample(centralEventBus);

        // Simple steps that shows how changing register options works
        // TODO: After the database and register will work, implement events for changing options

        System.out.println("DONE");
    }
}
