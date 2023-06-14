package com.example.weather_monitor.db;

import com.example.weather_monitor.APIWeatherManager;
import com.example.weather_monitor.data.Country;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBManager implements AutoCloseable {
    private final Session session;
    private final SessionFactory sessionFactory;
    private final int maxRowsInRegister;

    public DBManager() throws IOException, SQLException {
        final Properties properties = new Properties();
        String propFileName = "config.properties";
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

        if (inputStream != null) {
            properties.load(inputStream);
        } else {
            throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
        }

        Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.dialect", properties.getProperty("db.dialect"));
        configuration.setProperty("hibernate.connection.url", properties.getProperty("db.url"));
        configuration.setProperty("hibernate.connection.username", properties.getProperty("db.user"));
        configuration.setProperty("hibernate.connection.password", properties.getProperty("db.password"));
        configuration.setProperty("hibernate.hbm2ddl.auto", properties.getProperty("db.auto"));
        configuration.addAnnotatedClass(WeatherRecord.class);

        this.sessionFactory = configuration.buildSessionFactory();
        this.session = sessionFactory.openSession();

        maxRowsInRegister = Integer.parseInt(properties.getProperty("max_rows_in_register"));
    }

    public void uploadInstanceToDatabase() throws IOException {
        Transaction transaction = session.beginTransaction();
        session.persist(new WeatherRecord());
        transaction.commit();
    }

    public void addRecord(WeatherRecord weatherRecord) {
        Transaction transaction = session.beginTransaction();
        session.persist(weatherRecord);
        transaction.commit();
    }

    public List<WeatherRecord> getRecords() {
        String queryString = "FROM WeatherRecord ORDER BY time DESC LIMIT :maxRows";
        Query<WeatherRecord> query = session.createQuery(queryString, WeatherRecord.class);
        query.setParameter("maxRows", maxRowsInRegister);
        return query.list();
    }

    public void clearRecords() {
        Transaction transaction = session.beginTransaction();
        session.createQuery("DELETE FROM WeatherRecord").executeUpdate();
        transaction.commit();
    }

    public static void main(String[] args) {
        try(DBManager dbManager = new DBManager()) {
            for(int i=0; i<5; i++) {
                WeatherRecord weatherRecord = APIWeatherManager.getWeatherData(Country.Poland);
                dbManager.addRecord(weatherRecord);
                List<WeatherRecord> records = dbManager.getRecords();
                System.out.println(records);
            }
            dbManager.clearRecords();
            List<WeatherRecord> records = dbManager.getRecords();
            System.out.println(records);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void close() {
        session.close();
        sessionFactory.close();
    }
}
