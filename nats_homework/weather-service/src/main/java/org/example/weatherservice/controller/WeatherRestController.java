package org.example.weatherservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.weatherservice.model.Weather;
import org.example.weatherservice.subscriber.WeatherSubscriber;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WeatherRestController {

    private final WeatherSubscriber subscriber;

    @GetMapping("/api/weather")
    public ResponseEntity<Weather> getLatestWeather() {
        Weather weather = subscriber.getWeather();
        if (weather == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(weather);
    }
}
