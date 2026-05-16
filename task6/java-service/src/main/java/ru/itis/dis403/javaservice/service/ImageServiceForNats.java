package ru.itis.dis403.javaservice.service;

import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.Nats;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class ImageServiceForNats {
    private final List<String> imgList = new CopyOnWriteArrayList<>();

    private static final String NATS_URL = "nats://localhost:4222";

    private static final String SUBJECT = "request.image.mirror";

    private final Connection connection;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ImageServiceForNats() throws IOException, InterruptedException {
        this.connection = Nats.connect(NATS_URL);
        System.out.println("Nats connected");
    }

    public String processImage(byte[] image) {
        Map<String, Object> requestMap = Map.of(
                "service", "mirror",
                "image", Base64.getEncoder().encodeToString(image)
        );

        try {
            String jsonRequest = objectMapper.writeValueAsString(requestMap);

            Message reply = connection.request(SUBJECT, jsonRequest.getBytes(StandardCharsets.UTF_8), Duration.ofSeconds(10));
            String jsonResponse = new String(reply.getData(), StandardCharsets.UTF_8);
            Map<String, String> resultMap = objectMapper.readValue(jsonResponse, Map.class);

            if ("success".equals(resultMap.get("status"))) {
                imgList.add(resultMap.get("image"));
            }
            return jsonResponse;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Запрос прерван");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "{}";
    }

    public List<String> getImgList() {
        return imgList;
    }
}
