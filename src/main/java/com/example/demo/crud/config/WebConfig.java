package com.example.demo.crud.config;

import com.example.demo.crud.JwtFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final UploadProperties uploadProperties;

    public WebConfig(UploadProperties uploadProperties) {
        this.uploadProperties = uploadProperties;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/products/**")
                .addResourceLocations(Paths.get(uploadProperties.getProductsDir()).toUri().toString());

        registry.addResourceHandler("/uploads/payments/**")
                .addResourceLocations(Paths.get(uploadProperties.getPaymentDir()).toUri().toString());

        registry.addResourceHandler("/uploads/categories/**")
                .addResourceLocations(Paths.get(uploadProperties.getCategoriesDir()).toUri().toString());
    }

    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilterRegistration(JwtFilter filter) {
        FilterRegistrationBean<JwtFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(filter);
        registration.addUrlPatterns("/*");
        registration.setName("JwtFilter");
        registration.setOrder(1);
        return registration;
    }
}