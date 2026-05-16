package ru.itis.dis403.javaservice.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class ImageService {

    private final List<String> imgList = new CopyOnWriteArrayList<>();

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private static final String SERVER_URL = "http://127.0.0.1:5001/resize";
    private static final String CONTENT_TYPE = "image/jpeg";

    public void processImageBlocking(byte[] imageBytes) throws IOException, InterruptedException {
        HttpRequest request = buildRequest(imageBytes);

        HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());

        handleResponse(response);
    }

    public CompletableFuture<Void> processImageAsync(byte[] imageBytes) {
        HttpRequest request = buildRequest(imageBytes);

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofByteArray())
                .thenAccept(this::handleResponse)
                .exceptionally(ex -> {
                    Throwable cause = unwrap(ex);
                    System.out.println("Ошибка при обработке сообщения: " + cause.getMessage());
                    throw new RuntimeException("Не удалось обработать изображение", cause);
                });
    }

    public void processAllAsync(List<byte[]> images) {
        List<CompletableFuture<Void>> futures = images.stream()
                .map(this::processImageAsync)
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    private HttpRequest buildRequest(byte[] imageBytes) {
        return HttpRequest.newBuilder()
                .uri(URI.create(SERVER_URL))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", CONTENT_TYPE)
                .POST(HttpRequest.BodyPublishers.ofByteArray(imageBytes))
                .build();
    }

    private void handleResponse(HttpResponse<byte[]> response) {
        int statusCode = response.statusCode();

        if (statusCode == 200) {
            imgList.add(Base64.getEncoder().encodeToString(response.body()));
            System.out.println("Изображение обработано, статус: " + statusCode);
        } else {
            String errorBody = new String(response.body());
            throw new RuntimeException("Сервер вернул ошибку " + statusCode + ": " + errorBody);
        }
    }

    private Throwable unwrap(Throwable ex) {
        return (ex instanceof CompletionException && ex.getCause() != null) ?
                ex.getCause() : ex;
    }

    public List<String> getImgList() {
        return imgList;
    }
}
