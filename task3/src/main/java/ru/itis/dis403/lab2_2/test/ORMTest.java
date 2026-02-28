package ru.itis.dis403.lab2_2.test;

import ru.itis.dis403.lab2_2.context.model.*;
import ru.itis.dis403.lab2_2.orm.EntityManager;
import ru.itis.dis403.lab2_2.orm.EntityManagerFactory;
import java.math.BigDecimal;
import java.util.List;

public class ORMTest {

    public static void main(String[] args) {
        try {
            testORM();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testORM() throws Exception {
        System.out.println("=== START ORM TESTS ===\n");

        EntityManagerFactory factory = new EntityManagerFactory();
        EntityManager em = factory.getEntityManager();

        // ТЕСТ 1: Создание продукта
        System.out.println("TEST 1: Create Product");
        Product product1 = new Product("iPhone 15", "APL-001", Category.PHONE, new BigDecimal("99990.00"));
        Product savedProduct1 = em.save(product1);
        System.out.println("Saved: " + savedProduct1);
        assert savedProduct1.getId() != null : "ID должен быть сгенерирован";

        // ТЕСТ 2: Создание заказа
        System.out.println("\nTEST 2: Create Order");
        Order order1 = new Order(savedProduct1, 2, "Иванов И.И.");
        Order savedOrder1 = em.save(order1);
        System.out.println("Saved: " + savedOrder1);

        // ТЕСТ 3: Создание импорта
        System.out.println("\nTEST 3: Create ImportProduct");
        ImportProduct import1 = new ImportProduct(savedProduct1, 100, "Apple Inc.");
        ImportProduct savedImport1 = em.save(import1);
        System.out.println("Saved: " + savedImport1);

        // ТЕСТ 4: Поиск по ID
        System.out.println("\nTEST 4: Find by ID");
        Product foundProduct = em.find(Product.class, savedProduct1.getId());
        System.out.println("Found: " + foundProduct);
        assert foundProduct != null : "Продукт должен быть найден";

        // ТЕСТ 5: Получить все продукты
        System.out.println("\nTEST 5: Find All Products");
        Product product2 = new Product("Samsung TV", "SAM-002", Category.TV, new BigDecimal("55000.00"));
        em.save(product2);

        List<Product> allProducts = em.findAll(Product.class);
        System.out.println("All products (" + allProducts.size() + "):");
        for (Product p : allProducts) {
            System.out.println("  - " + p);
        }

        // ТЕСТ 6: Обновление продукта
        System.out.println("\nTEST 6: Update Product");
        foundProduct.setPrice(new BigDecimal("89990.00"));
        em.save(foundProduct);
        Product updatedProduct = em.find(Product.class, foundProduct.getId());
        System.out.println("Updated: " + updatedProduct);

        // ТЕСТ 7: Удаление
        System.out.println("\nTEST 7: Delete Order");
        em.remove(savedOrder1);
        Order deletedOrder = em.find(Order.class, savedOrder1.getId());
        System.out.println("Deleted order found: " + deletedOrder);
        assert deletedOrder == null : "Заказ должен быть удалён";

        em.close();
        factory.close();

        System.out.println("\n=== ALL TESTS PASSED ===");
    }
}