module com.example.weather_monitor {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires com.google.common;
    requires jakarta.persistence;
    requires lombok;

    opens com.example.weather_monitor to javafx.fxml;
    exports com.example.weather_monitor;
    exports com.example.weather_monitor.listener;
    opens com.example.weather_monitor.listener to javafx.fxml;
    exports com.example.weather_monitor.event;
    opens com.example.weather_monitor.event to javafx.fxml;
}