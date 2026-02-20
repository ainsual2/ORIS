package ru.itis.dis403.lab2_1.di.component;

import ru.itis.dis403.lab2_1.di.annotation.Component;
import ru.itis.dis403.lab2_1.di.model.Fruit;
import ru.itis.dis403.lab2_1.di.model.FruitType;
import ru.itis.dis403.lab2_1.di.model.Store;

@Component
public class Application {

    //@Inject
    private StoreService service;

    public Application(StoreService service) {
        this.service = service;
    }

    public void run() {

        service.add("I");
        service.add("II");

        Store storeI = service.findByName("I");
        service.addFruit(storeI, new Fruit("Яблоко", FruitType.APPLE), 1000);
        service.addFruit(storeI, new Fruit("Бананы", FruitType.BANANA), 2000);

        Store storeII = service.findByName("II");
        service.addFruit(storeI, new Fruit("Яблоко", FruitType.APPLE), 3000);
        service.addFruit(storeI, new Fruit("Апельсины", FruitType.ORANGE), 2000);

        service.getAll().forEach(System.out::println);
    }

}
