package ru.itis.dis403.lab2_1.di.config;

import ru.itis.dis403.lab2_1.di.annotation.Component;
import ru.itis.dis403.lab2_1.di.annotation.Controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class PathScan {

    public static List<Class<?>> find(String scannedPackage) {
        List<Class<?>> classes = new ArrayList<>();
        String scannedPath = scannedPackage.replace(".", "/");

        try {
            // Получаем все ресурсы (поддерживает и файлы, и JAR)
            Enumeration<URL> resources = Thread.currentThread()
                    .getContextClassLoader().getResources(scannedPath);

            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                String path = URLDecoder.decode(resource.getFile(), StandardCharsets.UTF_8.name());

                // Убираем протокол file:
                if (path.startsWith("file:")) {
                    path = path.substring(5);
                }

                // Убираем ! для случаев запуска из JAR
                if (path.contains("!")) {
                    path = path.split("!")[0];
                }

                File directory = new File(path);
                if (directory.exists() && directory.isDirectory()) {
                    classes.addAll(findClasses(directory, scannedPackage));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("=== PathScan: найдено классов с аннотациями: " + classes.size());
        return classes;
    }

    private static List<Class<?>> findClasses(File directory, String packageName) {
        List<Class<?>> classes = new ArrayList<>();

        if (!directory.exists()) {
            return classes;
        }

        File[] files = directory.listFiles();
        if (files == null) return classes;

        for (File file : files) {
            if (file.isDirectory()) {
                // Рекурсивный поиск в подпакетах (например, .controller)
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
                try {
                    Class<?> clazz = Class.forName(className);

                    // Проверяем на наличие нужных аннотаций
                    if (clazz.isAnnotationPresent(Component.class) ||
                            clazz.isAnnotationPresent(Controller.class)) {
                        classes.add(clazz);
                        System.out.println("  [OK] Найден класс: " + clazz.getSimpleName());
                    }
                } catch (ClassNotFoundException e) {
                    // Игнорируем
                }
            }
        }
        return classes;
    }
}