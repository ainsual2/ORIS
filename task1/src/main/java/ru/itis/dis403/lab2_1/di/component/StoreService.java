package ru.itis.dis403.lab2_1.di.component;

import ru.itis.dis403.lab2_1.di.annotation.Component;
import ru.itis.dis403.lab2_1.di.model.Basa;
import ru.itis.dis403.lab2_1.di.model.Fruit;
import ru.itis.dis403.lab2_1.di.model.FruitType;
import ru.itis.dis403.lab2_1.di.model.Store;
import java.util.List;

@Component
public class StoreService {
    private Basa basa = new Basa();

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

    public long getCountFruitByStore(String name, FruitType type) {
        return basa.getStores().stream()
                .filter(s -> s.getName().equals(name))
                .findFirst()
                .orElseThrow()
                .getFruits()
                .entrySet().stream().filter(e -> e.getKey().getType() == type)
                .count();
    }
}
