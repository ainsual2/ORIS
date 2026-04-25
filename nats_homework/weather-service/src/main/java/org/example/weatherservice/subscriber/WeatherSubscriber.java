package org.example.weatherservice.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Nats;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import org.example.weatherservice.model.Weather;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class WeatherSubscriber {

    @Value("${nats.url:nats://localhost:4222}")
    private String natsUrl;

    private Connection connection;

    @Getter
    private volatile Weather weather;

    @PostConstruct
    public void subscribe() throws Exception {
        System.out.println("Connecting to NATS at: " + natsUrl);
        connection = Nats.connect(natsUrl);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        Dispatcher dispatcher = connection.createDispatcher(msg -> {
            try {
                weather = objectMapper.readValue(msg.getData(), Weather.class);
                System.out.println("Received: " + weather.getCity()
                        + " | temp=" + weather.getTemp() + " | pressure= " + weather.getPressure()
                        + " | windSpeed= " + weather.getWindSpeed() + " | windDirection= " + weather.getWindDirection());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        dispatcher.subscribe("Weather");
        System.out.println("Subscribed to topic: Weather");
    }

    @PreDestroy
    public void close() throws InterruptedException {
        if (connection != null) connection.close();
    }
}
