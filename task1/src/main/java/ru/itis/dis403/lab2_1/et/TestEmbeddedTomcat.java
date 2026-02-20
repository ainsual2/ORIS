package ru.itis.dis403.lab2_1.et;

import jakarta.servlet.http.HttpServlet;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import ru.itis.dis403.lab2_1.di.config.Context;
import ru.itis.dis403.lab2_1.di.servlet.DispatcherServlet;

import java.io.File;

public class TestEmbeddedTomcat {
    public static void main(String[] args) {
        // 1. Инициализация нашего DI контейнера
        Context diContext = new Context();

        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir("temp");

        Connector conn = new Connector();
        conn.setPort(8090);
        tomcat.setConnector(conn);

        String contextPath = "";
        String docBase = new File(".").getAbsolutePath();

        // 2. ВАЖНО: Используем полное имя для Tomcat Context,
        // так как мы не импортировали org.apache.catalina.Context
        org.apache.catalina.Context tomcatContext = tomcat.addContext(contextPath, docBase);

        // 3. Создаем наш Диспетчер Сервлет, передавая DI-контекст
        DispatcherServlet dispatcherServlet = new DispatcherServlet(diContext);

        String servletName = "dispatcherServlet";

        // 4. Регистрируем сервлет в Tomcat
        tomcat.addServlet(contextPath, servletName, dispatcherServlet);

        // 5. Маппинг всех запросов на диспетчер
        tomcatContext.addServletMappingDecoded("/*", servletName);

        try {
            System.out.println("Запуск сервера на http://localhost:8090");
            tomcat.start();
            tomcat.getServer().await();
        } catch (LifecycleException e) {
            throw new RuntimeException(e);
        }
    }
}