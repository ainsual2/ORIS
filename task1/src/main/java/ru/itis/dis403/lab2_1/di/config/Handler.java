package ru.itis.dis403.lab2_1.di.config;

import java.lang.reflect.Method;

public class Handler {

    private Object controller;
    private Method method;

    public Handler(Object controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    public Object getController() {
        return controller;
    }

    public Method getMethod() {
        return method;
    }
}