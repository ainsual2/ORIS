package ru.itis.dis403.lab2_1.di.config;

import ru.itis.dis403.lab2_1.di.annotation.Component;
import ru.itis.dis403.lab2_1.di.annotation.Controller;
import ru.itis.dis403.lab2_1.di.annotation.GetMapping;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Context {

    private String scanPath = "ru.itis.dis403.lab2_1.di";

    // Хранилище созданных объектов (бинов)
    private Map<Class<?>, Object> components = new HashMap<>();

    // Маршрутизация: URL -> Обработчик (Контроллер + Метод)
    private Map<String, Handler> routes = new HashMap<>();

    public Context() {
        scanComponent();
    }

    public Object getComponent(Class clazz) {
        return components.get(clazz);
    }

    // Метод для получения обработчика по URL
    public Handler getHandler(String path) {
        return routes.get(path);
    }
    // В классе Context добавьте:
    public java.util.Set<String> getRoutes() {
        return routes.keySet();
    }

    private void scanComponent() {
        System.out.println("=== Начало сканирования пакета: " + scanPath);
        List<Class<?>> classes = PathScan.find(scanPath);
        System.out.println("=== Всего классов с аннотациями: " + classes.size());

        int countClasses = classes.size();
        while (countClasses > 0) {
            int createdCount = 0;
            objectNotFound:
            for (Class c : classes) {
                // Пропускаем, если уже создан
                if (components.get(c) != null) continue;

                // Проверяем, является ли класс Компонентом или Контроллером
                if (!c.isAnnotationPresent(Component.class) && !c.isAnnotationPresent(Controller.class)) {
                    continue;
                }

                Constructor constructor;
                try {
                    // Берем первый публичный конструктор
                    constructor = c.getConstructors()[0];
                } catch (Exception e) {
                    continue;
                }

                Class[] types = constructor.getParameterTypes();
                Object[] args = new Object[types.length];

                // Пытаемся найти зависимости
                for (int i = 0; i < types.length; ++i) {
                    args[i] = components.get(types[i]);
                    if (args[i] == null) {
                        continue objectNotFound;
                    }
                }

                try {
                    Object o = constructor.newInstance(args);
                    components.put(c, o);
                    createdCount++;
                    System.out.println("Bean создан: " + c.getSimpleName());

                    // Если это Контроллер, регистрируем его методы
                    if (c.isAnnotationPresent(Controller.class)) {
                        registerControllerMethods(o, c);
                    }

                } catch (Exception e) {
                    System.out.println("Ошибка создания " + c + ": " + e.getMessage());
                }
            }
            if (createdCount == 0 && countClasses > 0) {
                // Зависимости не найдены, выходим чтобы не зациклиться
                System.out.println("Не удалось создать оставшиеся компоненты (возможно, нет зависимостей).");
                break;
            }
            countClasses -= createdCount;
        }
    }

    private void registerControllerMethods(Object controllerInstance, Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(GetMapping.class)) {
                GetMapping mapping = method.getAnnotation(GetMapping.class);
                String path = mapping.value();
                // Сохраняем связку: Путь -> (Инстанс контроллера, Метод)
                routes.put(path, new Handler(controllerInstance, method));
                System.out.println("Маршрут зарегистрирован: " + path + " -> " + method.getName());
            }
        }
    }
}