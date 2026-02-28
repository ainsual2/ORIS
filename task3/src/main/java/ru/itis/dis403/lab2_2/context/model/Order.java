package ru.itis.dis403.lab2_2.context.model;

import ru.itis.dis403.lab2_2.orm.annotations.*;

@Entity("order_table")
public class Order {

    @Id
    @Column("id")
    private Long id;

    @ManyToOne
    @Column("product_id")
    private Product product;

    @Column("count")
    private Integer count;

    @Column("client")
    private String client;

    public Order() {}

    public Order(Product product, Integer count, String client) {
        this.product = product;
        this.count = count;
        this.client = client;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public Integer getCount() { return count; }
    public void setCount(Integer count) { this.count = count; }
    public String getClient() { return client; }
    public void setClient(String client) { this.client = client; }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", product=" + (product != null ? product.getName() : "null") +
                ", count=" + count +
                ", client='" + client + '\'' +
                '}';
    }
}