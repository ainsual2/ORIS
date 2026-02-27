package ru.itis.dis403.lab2_1.di.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.itis.dis403.lab2_1.di.component.StoreService;


import java.io.IOException;
import java.io.PrintWriter;

@Controller
public class HomeController {

    private final StoreService storeService;

    // Spring автоматически внедрит зависимость через конструктор
    public HomeController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/home")
    public void getHome(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.getWriter().write("HOME PAGE");
    }
}