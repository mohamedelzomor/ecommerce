package com.example.demo.crud.Models;

import jakarta.persistence.*;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private UserOrder order;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;


    public CartItem() {}

    public CartItem(UserOrder order, Product product, Integer quantity) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UserOrder getOrder() { return order; }
    public void setOrder(UserOrder order) { this.order = order; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getTotalPrice() {
    if (product != null && product.getPrice() != null) {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    } else {
        return BigDecimal.ZERO;
    }
}
}
