package ru.itis.dis403.lab2_2.orm;

import ru.itis.dis403.lab2_2.orm.annotations.Entity;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class EntityScanner {

    private final String basePackage;

    public EntityScanner(String basePackage) {
        this.basePackage = basePackage;
    }

    public List<Class<?>> scanEntities() {
        List<Class<?>> entities = new ArrayList<>();
        String packagePath = basePackage.replace(".", "/");

        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Enumeration<URL> resources = classLoader.getResources(packagePath);

            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                if ("file".equals(resource.getProtocol())) {
                    scanDirectory(new File(resource.getFile()), entities);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error scanning entities", e);
        }

        return entities;
    }

    private void scanDirectory(File directory, List<Class<?>> entities) {
        if (!directory.exists()) return;

        File[] files = directory.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                scanDirectory(file, entities);
            } else if (file.getName().endsWith(".class")) {
                String className = getClassName(file, basePackage);
                try {
                    Class<?> clazz = Class.forName(className);
                    if (clazz.isAnnotationPresent(Entity.class)) {
                        entities.add(clazz);
                        System.out.println("Found entity: " + clazz.getSimpleName());
                    }
                } catch (ClassNotFoundException e) {
                    // ignore
                }
            }
        }
    }

    private String getClassName(File file, String basePackage) {
        String path = file.getAbsolutePath();
        int idx = path.indexOf(basePackage.replace(".", File.separator));
        String relativePath = path.substring(idx);
        return relativePath.replace(File.separator, ".").replace(".class", "");
    }
}