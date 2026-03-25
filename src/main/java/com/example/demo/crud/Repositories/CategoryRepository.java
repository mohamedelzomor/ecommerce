package com.example.demo.crud.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.example.demo.crud.Models.Category;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Override
    @NonNull
    Optional<Category> findById(@NonNull Long id);

    Optional<Category> findByTitle(String title);

    boolean existsByTitle(String title);


}
