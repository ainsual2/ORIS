package ru.itis.dis403.lab2_1.di.server;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import ru.itis.dis403.lab2_1.di.servlet.DispatcherServlet;


public class Server {

    public static void main(String[] args) throws LifecycleException {

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);

        tomcat.getConnector();

        Context context = tomcat.addContext("", null);

        Tomcat.addServlet(context, "dispatcher", new DispatcherServlet());

        context.addServletMappingDecoded("/", "dispatcher");

        tomcat.start();

        System.out.println("Server started on http://localhost:8080");

        tomcat.getServer().await();
    }
}