package ru.itis.dis403.lab2_1.di;

import ru.itis.dis403.lab2_1.di.component.Application;
import ru.itis.dis403.lab2_1.di.config.Context;

public class Main {
    public static void main(String[] args) {
        Context context = new Context();

        Application application = (Application) context.getComponent(Application.class);
        application.run();
    }
}
