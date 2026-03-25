package com.example.demo.crud.Product;

import java.math.BigDecimal;

import org.springframework.web.multipart.MultipartFile;

public class ProductDTO {
    private Long id; 
    private String title;
    private BigDecimal price;
    private String description;
    private int quantity;
    private MultipartFile image;
    private Long categoryId; 

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public MultipartFile getImage() { return image; }
    public void setImage(MultipartFile image) { this.image = image; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
}
