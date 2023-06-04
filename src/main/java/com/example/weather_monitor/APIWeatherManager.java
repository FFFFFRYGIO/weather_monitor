package com.example.weather_monitor;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Properties;

import com.example.weather_monitor.data.Country;
import com.example.weather_monitor.db.WeatherRecord;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/* Class with everything about API connection */
public class APIWeatherManager {

    public static void main(String[] args) {
        // Sample testing if everything works

        Country country = Country.Czechia;

        WeatherRecord weatherRecord = getWeatherData(country);

        System.out.println("\n=== Weather Data Record Object ===");
        System.out.println(weatherRecord);

        JSONObject weatherData = getAllWeatherData(country);
        displayWeatherDataJSON(weatherData);
        try {
            displayWeatherDataNeeded(weatherData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static WeatherRecord getWeatherData(Country country) {
        JSONObject weatherData = getAllWeatherData(country);
        if (weatherData != null) {

            WeatherRecord weatherRecord = new WeatherRecord();

            String countryString = weatherData.getString("name");
            countryString = switch (countryString) {
                case "Luxembourg Province" -> "Luxembourg";
                case "Czech Republic" -> "Czechia";
                case "Republic of Lithuania" -> "Lithuania";
                default -> countryString;
            };
            weatherRecord.location = Country.valueOf(countryString);
            weatherRecord.time = LocalDateTime.now();
            weatherRecord.weatherCondition = weatherData.getJSONArray("weather").getJSONObject(0).getString("main");
            weatherRecord.temperature = weatherData.getJSONObject("main").getDouble("temp");
            weatherRecord.pressure = weatherData.getJSONObject("main").getInt("pressure");
            weatherRecord.cloudiness = weatherData.getJSONObject("clouds").getInt("all");

            return weatherRecord;
        }
        return null;
    }

    public static URL generateQueryUrl(Country country) throws IOException {

        final Properties properties = new Properties();
        String propFileName = "config.properties";
        InputStream inputStream = APIWeatherManager.class.getClassLoader().getResourceAsStream(propFileName);

        if (inputStream != null) {
            properties.load(inputStream);
        } else {
            throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
        }

        String BASE_URL = properties.getProperty("api.url");
        String API_KEY = properties.getProperty("api.key");

        String encodedCountry = URLEncoder.encode(String.valueOf(country), StandardCharsets.UTF_8);
        String queryUrl =  BASE_URL + "?q=" + encodedCountry + "&appid=" + API_KEY + "&units=metric";
        return new URL(Objects.requireNonNull(queryUrl));
    }

    public static JSONObject getAllWeatherData(Country country) {
        try {
            URL url = generateQueryUrl(country);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();
                connection.disconnect();

                return new JSONObject(response.toString());
            } else {
                System.out.println("Failed to retrieve weather data. Response code: " + connection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    public static void displayWeatherDataJSON(JSONObject weatherData) {
        if (weatherData != null) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String prettyJson = gson.toJson(weatherData);

            System.out.println("\n=== Weather Data JSON ===");
            System.out.println(prettyJson);
        } else {
            System.out.println("No weather data.");
        }
    }

    public static void displayWeatherDataNeeded(JSONObject weatherData) throws Exception {
        if (weatherData != null) {
            JSONObject jsonObject = new JSONObject(weatherData);

            if (jsonObject.has("main") && jsonObject.has("wind")) {
                JSONObject mainObject = jsonObject.getJSONObject("main");

                System.out.println("\n=== Weather Data Needed ===");

                String name = jsonObject.getString("name");
                System.out.println("Location: " + name);

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
