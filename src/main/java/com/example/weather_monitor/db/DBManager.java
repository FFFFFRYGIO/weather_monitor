package com.example.weather_monitor.db;

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
    }

//    public void printAllTables() throws SQLException {
//        String sqlQuery = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'";
//
//        session.createQuery(sqlQuery, String.class).list();
//        System.out.println(session.createNativeQuery(sqlQuery, String.class).list());
//
//    }

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
        Query<WeatherRecord> query = session.createQuery("FROM WeatherRecord", WeatherRecord.class);
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
                dbManager.addRecord(new WeatherRecord());
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
