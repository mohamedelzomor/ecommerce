package com.example.demo.crud.Models;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_orders")
public class UserOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    private String status;

    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    private BigDecimal totalPrice ;

    private String paymentImage; 

    public UserOrder() {}

    public UserOrder(User user, String status) {
        this.user = user;
        this.status = status;
    }

    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<CartItem> getCartItems() { return cartItems; }
    public void setCartItems(List<CartItem> cartItems) { this.cartItems = cartItems; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    
    
    public void calculateTotalPrice() {
    if (cartItems != null && !cartItems.isEmpty()) {
        totalPrice = cartItems.stream()
                .map(CartItem::getTotalPrice)      
                .reduce(BigDecimal.ZERO, BigDecimal::add); 
    } else {
        totalPrice = BigDecimal.ZERO;
    }
}


    public void addCartItem(CartItem item) {
        if (item != null) {
            cartItems.add(item);
            item.setOrder(this);
            calculateTotalPrice();
        }
    }


    public void removeCartItem(CartItem item) {
        if (item != null) {
            cartItems.remove(item);
            item.setOrder(null);
            calculateTotalPrice();
        }
    }

    public String getPaymentImage() {
        return paymentImage;
    }
    public void setPaymentImage(String paymentImage) {
        this.paymentImage = paymentImage;
    }
    
}
