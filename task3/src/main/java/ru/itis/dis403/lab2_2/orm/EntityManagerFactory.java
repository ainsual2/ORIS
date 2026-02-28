package ru.itis.dis403.lab2_2.orm;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.itis.dis403.lab2_2.orm.annotations.Entity;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class EntityManagerFactory {

    private HikariDataSource dataSource;
    private List<Class<?>> entities;

    public EntityManagerFactory() throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5434/lab2");
        config.setUsername("postgres");
        config.setPassword("postgres");
        config.setConnectionTimeout(50000);
        config.setMaximumPoolSize(10);
        dataSource = new HikariDataSource(config);

        EntityScanner scanner = new EntityScanner("ru.itis.dis403.lab2_2.context.model");
        entities = scanner.scanEntities();

        System.out.println("Found " + entities.size() + " entities:");
        for (Class<?> entity : entities) {
            Entity annotation = entity.getAnnotation(Entity.class);
            String tableName = annotation != null ? annotation.value() : entity.getSimpleName().toLowerCase();
            System.out.println("  - " + entity.getSimpleName() + " -> " + tableName);
        }
    }

    public EntityManager getEntityManager() {
        try {
            Connection connection = dataSource.getConnection();
            return new EntityManagerImpl(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    public List<Class<?>> getEntities() {
        return entities;
    }
}