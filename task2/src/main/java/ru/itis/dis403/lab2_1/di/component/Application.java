package ru.itis.dis403.lab2_1.di.component;

import org.springframework.stereotype.Component;

@Component
public class Application {

    private final StoreService service;

    public Application(StoreService service) {
        this.service = service;
        run();
    }

    public void run() {
        System.out.println("=== Приложение запущено ===");
        service.getAll().forEach(System.out::println);
    }
}