package ru.itis.dis403.lab2_1.di.component;

import org.springframework.stereotype.Component;
import ru.itis.dis403.lab2_1.di.model.Basa;
import ru.itis.dis403.lab2_1.di.model.Fruit;
import ru.itis.dis403.lab2_1.di.model.FruitType;
import ru.itis.dis403.lab2_1.di.model.Store;

import java.util.List;

@Component
public class StoreService {
    private Basa basa = new Basa();

    public StoreService() {
        // Инициализация данных при создании бина
        add("I");
        add("II");

        Store storeI = findByName("I");
        Store storeII = findByName("II");

        if (storeI != null) {
            addFruit(storeI, new Fruit("Яблоко", FruitType.APPLE), 1000);
            addFruit(storeI, new Fruit("Бананы", FruitType.BANANA), 2000);
        }
        if (storeII != null) {
            addFruit(storeII, new Fruit("Яблоко", FruitType.APPLE), 3000);
            addFruit(storeII, new Fruit("Апельсины", FruitType.ORANGE), 2000);
        }
    }

    public void add(String name) {
        basa.getStores().add(new Store(name));
    }

    public void addFruit(Store store, Fruit fruit, Integer count) {
        store.getFruits().put(fruit, count);
    }

    public List<Store> getAll() {
        return basa.getStores();
    }

    public Store findByName(String name) {
        return basa.getStores().stream()
                .filter(s -> s.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}