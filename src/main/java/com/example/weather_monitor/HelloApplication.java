package com.example.weather_monitor;

import com.example.weather_monitor.event.RegisterWeatherEvent;
import com.example.weather_monitor.listener.CountryThreadsListener;
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

    public static void main(String[] args) {
        // launch();

        // CentralEventBus
        CentralEventBus centralEventBus = new CentralEventBus();

        // CountryThreadsListener
        CountryThreadsListener countryThreadsListener = new CountryThreadsListener();
        centralEventBus.registerNewListener(countryThreadsListener);

        // WeatherUpdatesListener
        WeatherUpdatesListener weatherUpdatesListener = new WeatherUpdatesListener();
        centralEventBus.registerNewListener(weatherUpdatesListener);

        // RegisterListener
        RegisterListener registerListener = new RegisterListener();
        centralEventBus.registerNewListener(registerListener);

        // Testing events
        CountryWeatherThread countryWeatherThread = new CountryWeatherThread(centralEventBus);
        countryWeatherThread.publishEvent(new RegisterWeatherEvent(11.11F, true));
    }
}
