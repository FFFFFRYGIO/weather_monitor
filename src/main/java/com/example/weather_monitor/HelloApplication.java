package com.example.weather_monitor;

import com.example.weather_monitor.db.Country;
import com.example.weather_monitor.listener.CountryThreadsManageListener;
import com.example.weather_monitor.listener.RegisterListener;
import com.example.weather_monitor.listener.WeatherUpdatesListener;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

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

        System.out.println("DONE");
    }

    public static void main(String[] args) {
        // launch();

        // CentralEventBus
        CentralEventBus centralEventBus = new CentralEventBus();

        // CountryThreadsListener
        CountryThreadsManageListener countryThreadsManageListener = new CountryThreadsManageListener();
        centralEventBus.registerNewListener(countryThreadsManageListener);

        // WeatherUpdatesListener
        WeatherUpdatesListener weatherUpdatesListener = new WeatherUpdatesListener();
        centralEventBus.registerNewListener(weatherUpdatesListener);

        // RegisterListener
        RegisterListener registerListener = new RegisterListener();
        centralEventBus.registerNewListener(registerListener);

        // Simple walkthrough that shows how Threads works
        // ThreadWorkingExample(centralEventBus);


    }
}
