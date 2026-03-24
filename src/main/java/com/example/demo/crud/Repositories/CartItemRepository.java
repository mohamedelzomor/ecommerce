package com.example.demo.crud.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.crud.Models.CartItem;
import java.util.List;


public interface CartItemRepository extends JpaRepository<CartItem, Long> {
List<CartItem> findByOrder_Id(Long orderId);

}
