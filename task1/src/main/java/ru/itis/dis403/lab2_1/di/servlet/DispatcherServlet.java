package ru.itis.dis403.lab2_1.di.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.dis403.lab2_1.di.config.Context;

import java.io.IOException;
import java.io.PrintWriter;

public class DispatcherServlet extends HttpServlet {

    private final Context context;

    // Передаем контекст при создании сервлета
    public DispatcherServlet(Context context) {
        this.context = context;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws jakarta.servlet.ServletException, IOException {

        // 1. Получаем путь и нормализуем его
        String path = req.getRequestURI();

        // Убираем context path если он есть
        String contextPath = req.getContextPath();
        if (contextPath != null && !contextPath.isEmpty() && path.startsWith(contextPath)) {
            path = path.substring(contextPath.length());
        }

        // Убираем trailing slash (кроме корня "/")
        if (path.length() > 1 && path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }

        // Для отладки: выводим в консоль что пришло и что ищем
        System.out.println(">>> Запрос: " + req.getMethod() + " " + path);
        System.out.println(">>> Доступные маршруты: " + context.getRoutes());

        Context.Handler handler = context.getHandler(path);
        resp.setContentType("text/html; charset=utf-8");

        if (handler != null) {
            try {
                handler.method.invoke(handler.controller, req, resp);
            } catch (Exception e) {
                e.printStackTrace();
                PrintWriter writer = resp.getWriter();
                writer.println("<h1>500 Error</h1><pre>" + e.getMessage() + "</pre>");
            }
        } else {
            // 404 с подсказкой доступных маршрутов
            resp.setStatus(404);
            PrintWriter writer = resp.getWriter();
            writer.println("<h1>404 Not Found</h1>");
            writer.println("<p>Ресурс <b>" + path + "</b> не найден.</p>");
            writer.println("<p>Зарегистрированные маршруты:</p><ul>");
            for (String route : context.getRoutes()) {
                writer.println("<li><a href='" + route + "'>" + route + "</a></li>");
            }
            writer.println("</ul>");
        }
    }
}