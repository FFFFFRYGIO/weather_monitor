package com.example.weather_monitor;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Properties;

import com.example.weather_monitor.data.Country;
import com.example.weather_monitor.db.WeatherRecord;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/* Class with everything about API connection */
public class ApiWeatherConnection {
    private static final String CONFIG_FILE_PATH = "src/main/resources/config.properties";
    private static final String API_KEY_PROPERTY = "api.key";
    private static final String API_KEY = loadApiKey();
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    public static void main(String[] args) {
        // Sample testing if everything works

        Country country = Country.Poland;

        WeatherRecord weatherRecord = getWeatherData(country);

        System.out.println("\n=== Weather Data Record Object ===");
        System.out.println(weatherRecord.toString());

        String weatherData = getAllWeatherData(country);
        displayWeatherDataJSON(weatherData);
        try {
            displayWeatherDataNeeded(weatherData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String loadApiKey() {
        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(CONFIG_FILE_PATH)) {
            properties.load(fileInputStream);
            return properties.getProperty(API_KEY_PROPERTY);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static WeatherRecord getWeatherData(Country country) {
        String weatherData = getAllWeatherData(country);
        if (weatherData != null) {
            JSONObject jsonObject = new JSONObject(weatherData);
            JSONObject mainObject = jsonObject.getJSONObject("main");
            WeatherRecord weatherRecord = new WeatherRecord();

            weatherRecord.location = Country.valueOf(jsonObject.getString("name"));
            var timestamp = Instant.ofEpochSecond(jsonObject.getLong("dt"));
            weatherRecord.measurementTime = LocalDateTime.ofInstant(timestamp, ZoneId.systemDefault());
            weatherRecord.weatherCondition = jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
            weatherRecord.temperature = mainObject.getDouble("temp");
            weatherRecord.pressure = mainObject.getInt("pressure");
            weatherRecord.cloudiness = jsonObject.getJSONObject("clouds").getInt("all");

            return weatherRecord;
        }
        return null;
    }

    public static String getAllWeatherData(Country country) {
        String queryUrl = generateQueryUrl(String.valueOf(country));

        try {
            URL url = new URL(Objects.requireNonNull(queryUrl));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();
                connection.disconnect();

                return response.toString();
            } else {
                System.out.println("Failed to retrieve weather data. Response code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String generateQueryUrl(String country) {
        String encodedCountry = URLEncoder.encode(country, StandardCharsets.UTF_8);
        return BASE_URL + "?q=" + encodedCountry + "&appid=" + API_KEY + "&units=metric";
    }

    public static void displayWeatherDataJSON(String weatherData) {
        if (weatherData != null) {
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = jsonParser.parse(weatherData).getAsJsonObject();

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String prettyJson = gson.toJson(jsonObject);

            System.out.println("\n=== Weather Data JSON ===");
            System.out.println(prettyJson);
        } else {
            System.out.println("Brak danych pogodowych.");
        }
    }

    public static void displayWeatherDataNeeded(String weatherData) throws Exception {
        if (weatherData != null) {
            JSONObject jsonObject = new JSONObject(weatherData);

            if (jsonObject.has("main") && jsonObject.has("wind")) {
                JSONObject mainObject = jsonObject.getJSONObject("main");

                System.out.println("\n=== Weather Data Needed ===");

                String name = jsonObject.getString("name");
                System.out.println("Location: " + name);

                long timestamp = jsonObject.getLong("dt");
                LocalDateTime measurementTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault());
                String formattedTime = measurementTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                System.out.println("Measurement Time: " + formattedTime);

                JSONArray weatherArray = jsonObject.getJSONArray("weather");
                if (weatherArray.length() > 0) {
                    JSONObject weatherObject = weatherArray.getJSONObject(0);
                    System.out.println("Weather Condition: " + weatherObject.getString("main"));
                }

                System.out.println("Temperature: " + mainObject.getDouble("temp") + " °C");
                System.out.println("Pressure: " + mainObject.getInt("pressure") + " hPa");

                JSONObject cloudsObject = jsonObject.getJSONObject("clouds");
                System.out.println("Cloudiness: " + cloudsObject.getInt("all") + "%");

            } else {
                throw new Exception("Failed to print weather data");
            }
        } else {
            throw new Exception("No weather data");
        }
    }
}
