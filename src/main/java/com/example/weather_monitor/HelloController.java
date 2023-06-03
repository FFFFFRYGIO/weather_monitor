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
import javafx.scene.control.TextField;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static com.example.weather_monitor.data.RegisterOption.clear_register;
import static com.example.weather_monitor.data.RegisterOption.toggle_register;

public class HelloController {
    private final CentralEventBus centralEventBus = new CentralEventBus();
    private static final int GENERAL_PERIOD = loadGeneralPeriod();
    private final CountryThreadsManageListener countryThreadsManageListener = new CountryThreadsManageListener(centralEventBus, GENERAL_PERIOD);
    private final WeatherUpdatesListener weatherUpdatesListener = new WeatherUpdatesListener();
    private final RegisterListener registerListener = new RegisterListener();


    private static int loadGeneralPeriod() {
        String configFilePath = "src/main/resources/config.properties";
        String generalPeriodProperty = "general_period";
        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(configFilePath)) {
            properties.load(fileInputStream);
            return Integer.parseInt(properties.getProperty(generalPeriodProperty));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }
    @FXML
    private Button buttonAustria;

    @FXML
    private Button buttonBelarus;

    @FXML
    private Button buttonBelgium;

    @FXML
    private Button buttonCroatia;

    @FXML
    private Button buttonCzechia;

    @FXML
    private Button buttonDenmark;

    @FXML
    private Button buttonFrance;

    @FXML
    private Button buttonGermany;

    @FXML
    private Button buttonHungary;

    @FXML
    private Button buttonItaly;

    @FXML
    private Button buttonLithuania;

    @FXML
    private Button buttonLuxembourg;

    @FXML
    private Button buttonNetherlands;

    @FXML
    private Button buttonPoland;

    @FXML
    private Button buttonRomania;

    @FXML
    private Button buttonSerbia;

    @FXML
    private Button buttonSlovakia;

    @FXML
    private Button buttonSlovenia;

    @FXML
    private Button buttonSwitzerland;

    @FXML
    private Button buttonUkraine;

    @FXML
    private Button clearRegisterButton;

    @FXML
    public TextField monitoredCountriesPrompt;

    @FXML
    private TextField registerPrompt;

    @FXML
    private Button startStopRegisterButton;

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
