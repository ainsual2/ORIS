package ru.itis.dis403.lab2_2.context.model;

import ru.itis.dis403.lab2_2.orm.annotations.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity("product")
public class Product {

    @Id
    @Column("id")
    private Long id;

    @Column("name")
    private String name;

    @Column("articul")
    private String articul;

    @Column("category")
    private Category category;

    @Column("price")
    private BigDecimal price;

    public Product() {}

    public Product(String name, String articul, Category category, BigDecimal price) {
        this.name = name;
        this.articul = articul;
        this.category = category;
        this.price = price;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getArticul() { return articul; }
    public void setArticul(String articul) { this.articul = articul; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(articul, product.articul);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(articul);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", articul='" + articul + '\'' +
                ", category=" + category +
                ", price=" + price +
                '}';
    }
}