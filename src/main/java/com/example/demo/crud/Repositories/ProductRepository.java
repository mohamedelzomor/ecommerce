package com.example.demo.crud.Repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.crud.Models.Product;
import com.example.demo.crud.Models.Category;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // البحث عن منتج بالعنوان
    Optional<Product> findByTitle(String title);

    // ✅ البحث عن المنتجات حسب كائن الفئة نفسه
    List<Product> findByCategory(Category category);

    // ✅ أو لو حابب تبحث باسم الفئة مباشرة (من خلال العلاقة)
    List<Product> findByCategory_Title(String title);

    // ✅ أو لو حابب تبحث بمعرف الفئة مباشرة (من خلال العلاقة)
    List<Product> findByCategoryId(Long categoryId);
}
