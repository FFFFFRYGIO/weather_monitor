package com.example.weather_monitor;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

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

    public static void main(String[] args) {
        launch();
    }
}
