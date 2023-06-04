package com.example.weather_monitor.db;

import com.example.weather_monitor.data.Country;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Data
/* Weather record model for database */
public class WeatherRecord {
    @Id
    @GeneratedValue
    private Long id;
    public Country location;
    public LocalDateTime time;
    public String weatherCondition;
    public double temperature;
    public int pressure;
    public int cloudiness;
}
