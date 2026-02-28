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
        resp.setContentType("text/html; charset=utf-8");
        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        out.println("<h1>Страница Home</h1>");
        out.println("<p>Здесь информация о магазинах:</p>");
        out.println("<ul>");
        // Пример использования сервиса
        if (storeService != null) {
            storeService.getAll().forEach(store ->
                    out.println("<li>Магазин: " + store.getName() + "</li>")
            );
        }
        out.println("</ul>");
        out.println("<a href='/index'>Назад на Index</a>");
        out.println("</body></html>");
    }
    @GetMapping("/mag1")
    public void getMag1(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=utf-8");
        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        out.println("<h1>Страница Home</h1>");
        out.println("<p>Здесь информация о магазинах:</p>");
        out.println("<ul>");
        // Пример использования сервиса
        if (storeService != null) {
            storeService.getAll().forEach(store ->
                    out.println("<li>Магазин: " + store.getName() + "</li>")
            );
        }
        out.println("</ul>");
        out.println("<a href='/index'>Назад на Index</a>");
        out.println("</body></html>");
    }
}