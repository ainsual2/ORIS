package ru.itis.dis403.lab2_1.di.server;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;



public class Server {

    public static void main(String[] args) throws LifecycleException {
        WebApplicationContext springContext = createSpringContext();

        Tomcat tomcat = new Tomcat();

        tomcat.setPort(8080);
        tomcat.getConnector();

        String contextPath = "";
        String docBase = new java.io.File(".").getAbsolutePath();
        Context context = tomcat.addContext(contextPath, docBase);

        DispatcherServlet dispatcherServlet = new DispatcherServlet(springContext);

        Wrapper wrapper = context.createWrapper();
        wrapper.setName("dispatcher");
        wrapper.setServlet(dispatcherServlet);
        wrapper.setLoadOnStartup(1);

        context.addChild(wrapper);


        context.addServletMappingDecoded("/", "dispatcher");


        tomcat.start();
        System.out.println("Server started on http://localhost:8080");
        tomcat.getServer().await();
    }
    private static WebApplicationContext createSpringContext() {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        // Укажите базовый пакет для сканирования
        context.scan("ru.itis.dis403.lab2_1");
        context.refresh();

        return context;
    }
}

