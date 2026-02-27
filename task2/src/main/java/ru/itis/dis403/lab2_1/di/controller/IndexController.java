package ru.itis.dis403.lab2_1.di.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.io.PrintWriter;

@Controller
public class IndexController {

    @GetMapping("/index")
    public void getIndex(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.getWriter().write("INDEX PAGE");
    }

    @GetMapping("/")
    public void getRoot(HttpServletResponse resp) throws IOException {
        resp.sendRedirect("/index");
    }
}