module com.example.weather_monitor {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires com.google.common;
    requires jakarta.persistence;
    requires lombok;
    requires org.json;
    requires com.google.gson;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires org.apache.commons.io;

    exports com.example.weather_monitor.data;
    exports com.example.weather_monitor.db;
    opens com.example.weather_monitor.db to org.hibernate.orm.core;
    opens com.example.weather_monitor to javafx.fxml;
    exports com.example.weather_monitor;
    exports com.example.weather_monitor.listener;
    opens com.example.weather_monitor.listener to javafx.fxml;
    exports com.example.weather_monitor.event;
    opens com.example.weather_monitor.event to javafx.fxml;
}