package com.example.weather_monitor.db;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.Instant;

@Entity
@Data
public class WeatherRecord {
    @Id
    @GeneratedValue
    private Long id;
    private Country country;
    private Instant datetime;
    private float temperature;
    private boolean isWindy;
}
