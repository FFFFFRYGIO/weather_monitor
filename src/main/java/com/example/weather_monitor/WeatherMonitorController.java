package com.example.weather_monitor;

import com.example.weather_monitor.data.Country;
import com.example.weather_monitor.event.CountryRecordToggleEvent;
import com.example.weather_monitor.event.RegisterConfigEvent;
import com.example.weather_monitor.listener.CountryThreadsManageListener;
import com.example.weather_monitor.listener.RegisterListener;
import com.example.weather_monitor.listener.WeatherUpdatesListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static com.example.weather_monitor.data.RegisterOption.clear_register;
import static com.example.weather_monitor.data.RegisterOption.toggle_register;

public class WeatherMonitorController {
    private final CentralEventBus centralEventBus = new CentralEventBus();
    private static final int GENERAL_PERIOD = loadGeneralPeriod();
    private final CountryThreadsManageListener countryThreadsManageListener = new CountryThreadsManageListener(centralEventBus, GENERAL_PERIOD);
    private final WeatherUpdatesListener weatherUpdatesListener = new WeatherUpdatesListener();
    private final RegisterListener registerListener = new RegisterListener(this);


    private static int loadGeneralPeriod() {
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

        return Integer.parseInt(properties.getProperty("general_period"));
    }

    public void setRegisterPromptText(String text) {
        registerPrompt.setText(text);
    }

    @FXML
    private TextArea monitoredCountriesPrompt;

    @FXML
    private TextArea registerPrompt;

    @FXML
    void CountryButtonHandle(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String countryName = clickedButton.getText();
        Country country = Country.valueOf(countryName);
        CountryRecordToggleEvent countryRecordToggleEvent = new CountryRecordToggleEvent(country);
        centralEventBus.publishEventFromListener(countryRecordToggleEvent);
        monitoredCountriesPrompt.setText(CountryThreadsManageListener.getUpdateListOfMonitoredCountries());
    }

    @FXML
    void clearRegisterButtonHandle(ActionEvent event) {
        RegisterConfigEvent registerConfigEvent = new RegisterConfigEvent(clear_register);
        centralEventBus.publishEventFromListener(registerConfigEvent);
    }

    @FXML
    void startStopRegisterButtonHandle(ActionEvent event) {
        RegisterConfigEvent registerConfigEvent = new RegisterConfigEvent(toggle_register);
        centralEventBus.publishEventFromListener(registerConfigEvent);
    }

    @FXML
    void initialize() {
        centralEventBus.registerNewListener(countryThreadsManageListener);
        centralEventBus.registerNewListener(weatherUpdatesListener);
        centralEventBus.registerNewListener(registerListener);
    }


}
