package ru.itis.dis403.lab2_1.di.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.dis403.lab2_1.di.annotation.Controller;
import ru.itis.dis403.lab2_1.di.annotation.GetMapping;

import java.io.IOException;
import java.io.PrintWriter;

@Controller
public class IndexController {

    @GetMapping("/index")
    public void getIndex(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=utf-8");
        PrintWriter out = resp.getWriter();
        out.println("<html><body>");
        out.println("<h1>Главная страница (Index)</h1>");
        out.println("<p>Добро пожаловать в домашнее приложение!</p>");
        out.println("<a href='/home'>Перейти на Home</a>");
        out.println("</body></html>");
    }

    @GetMapping("/")
    public void getRoot(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Перенаправление на индекс при заходе на корень
        resp.sendRedirect("/index");
    }
}