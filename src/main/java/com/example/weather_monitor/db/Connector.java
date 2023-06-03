package com.example.weather_monitor.db;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Connector {
    private final String connectionString;

    public Connector() throws IOException {
        final Properties properties = new Properties();
        String propFileName = "config.properties";
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

        if (inputStream != null) {
            properties.load(inputStream);
        } else {
            throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
        }

        this.connectionString = properties.getProperty("db.url");
    }

    public static void main(String[] args) {
        // Sample testing if everything works

        Connector connector = null;
        try {
            connector = new Connector();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String connectionString = connector.getConnectionString();

        try (Connection connection = DriverManager.getConnection(connectionString)) {
            DatabaseMetaData metadata = connection.getMetaData();
            ResultSet tables = metadata.getTables(null, null, null, new String[]{"TABLE"});

            System.out.println("Tables in the database:");
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                System.out.println(tableName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public String getConnectionString() {
        return connectionString;
    }
}
