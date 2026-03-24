// package com.example.demo.crud.Models;

// import jakarta.persistence.*;
// import java.util.ArrayList;
// import java.util.List;

// @Entity
// @Table(name = "cart")
// public class Cart {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     // لو لسه مفيش نظام مستخدمين، خليه nullable مؤقتًا
//     @Column(nullable = true)
//     private Long userId;

//     @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
//     private List<CartItem> items = new ArrayList<>();

//     // =======================
//     // Constructors
//     // =======================
//     public Cart() {}

//     public Cart(Long userId) {
//         this.userId = userId;
//     }

//     // =======================
//     // Getters and Setters
//     // =======================
//     public Long getId() {
//         return id;
//     }

//     public void setId(Long id) {
//         this.id = id;
//     }

//     public Long getUserId() {
//         return userId;
//     }

//     public void setUserId(Long userId) {
//         this.userId = userId;
//     }

//     public List<CartItem> getItems() {
//         return items;
//     }

//     public void setItems(List<CartItem> items) {
//         this.items = items;
//     }

//     // =======================
//     // Helper Methods
//     // =======================
//     public void addItem(CartItem item) {
//         items.add(item);
//         item.setCart(this);
//     }

//     public void removeItem(CartItem item) {
//         items.remove(item);
//         item.setCart(null);
//     }
// }
