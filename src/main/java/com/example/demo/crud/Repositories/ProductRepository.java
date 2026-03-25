package com.example.demo.crud.Repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.crud.Models.Product;
import com.example.demo.crud.Models.Category;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {


    

    Optional<Product> findByTitle(String title);


    List<Product> findByCategory(Category category);


    List<Product> findByCategory_Title(String title);

    List<Product> findByCategoryId(Long categoryId);
}
