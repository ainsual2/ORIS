package ru.itis.dis403.lab2_2.context.model;

import ru.itis.dis403.lab2_2.orm.annotations.*;

@Entity("import_product")
public class ImportProduct {

    @Id
    @Column("id")
    private Long id;

    @ManyToOne
    @Column("product_id")
    private Product product;

    @Column("count")
    private Integer count;

    @Column("supplier")
    private String supplier;

    public ImportProduct() {}

    public ImportProduct(Product product, Integer count, String supplier) {
        this.product = product;
        this.count = count;
        this.supplier = supplier;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public Integer getCount() { return count; }
    public void setCount(Integer count) { this.count = count; }
    public String getSupplier() { return supplier; }
    public void setSupplier(String supplier) { this.supplier = supplier; }

    @Override
    public String toString() {
        return "ImportProduct{" +
                "id=" + id +
                ", product=" + (product != null ? product.getName() : "null") +
                ", count=" + count +
                ", supplier='" + supplier + '\'' +
                '}';
    }
}