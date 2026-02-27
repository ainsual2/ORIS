package ru.itis.dis403.lab2_1.di.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.dis403.lab2_1.di.config.Context;
import ru.itis.dis403.lab2_1.di.config.Handler;

import java.io.IOException;

public class DispatcherServlet extends HttpServlet {

    private Context context;

    @Override
    public void init() {
        context = new Context();
        System.out.println("Context initialized");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String path = req.getRequestURI();
        System.out.println("Request: " + path);

        Handler handler = context.getHandler(path);

        if (handler == null) {

            String resourcePath = "/static" + path;

            var stream = getClass().getResourceAsStream(resourcePath);

            if (stream != null) {
                resp.setStatus(200);
                stream.transferTo(resp.getOutputStream());
                return;
            }

            resp.setStatus(404);
            resp.getWriter().write("404 NOT FOUND");
            return;
        }

        try {
            handler.getMethod().invoke(
                    handler.getController(),
                    req,
                    resp
            );
        } catch (Exception e) {


            resp.setStatus(500);
            resp.getWriter().write("500 SERVER ERROR");
            e.printStackTrace();
        }
    }
}