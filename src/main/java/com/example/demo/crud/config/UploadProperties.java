package com.example.demo.crud.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "upload")
public class UploadProperties {

    private String productsDir;
    private String paymentDir;
    private String categoriesDir;

    public String getProductsDir() {
        return productsDir;
    }

    public void setProductsDir(String productsDir) {
        this.productsDir = productsDir;
    }

    public String getPaymentDir() {
        return paymentDir;
    }

    public void setPaymentDir(String paymentDir) {
        this.paymentDir = paymentDir;
    }

    public String getCategoriesDir() {
        return categoriesDir;
    }

    public void setCategoriesDir(String categoriesDir) {
        this.categoriesDir = categoriesDir;
    }
}