package org.example.weatherpublisher.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.nats.client.Connection;
import io.nats.client.Nats;
import org.example.weatherpublisher.model.Weather;

import java.time.LocalDateTime;
import java.util.Random;

public class WeatherPublisher {

    public static void main(String[] args) {
        String subject = "Weather";

        String natsUrl = System.getenv().getOrDefault("NATS_URL", "nats://localhost:4222");
        System.out.println("Connection to NATS at: " + natsUrl);

        try (Connection connection = Nats.connect(natsUrl)) {
            Random random = new Random();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            System.out.println("Connected! Publishing weather data every second...");

            while (true) {
                Weather weather = Weather.builder()
                        .city("Казань")
                        .temp(10.0 + random.nextDouble() * 2 - 1)
                        .pressure(744 + random.nextDouble() * 4 - 2)
                        .windSpeed(3 + random.nextDouble() * 4 - 2)
                        .windDirection("СЗ")
                        .dateTime(LocalDateTime.now())
                        .build();

                byte[] msg = objectMapper.writeValueAsBytes(weather);
                connection.publish(subject, msg);
                System.out.println("Publisher: " + new String(msg));
                Thread.sleep(1000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
