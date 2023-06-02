module com.example.weather_monitor {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.weather_monitor to javafx.fxml;
    exports com.example.weather_monitor;
}