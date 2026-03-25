package com.example.demo.crud.Repositories;

import com.example.demo.crud.Models.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserOrderRepository extends JpaRepository<UserOrder, Long> {

    List<UserOrder> findByUserId(Long userId);

    Optional<UserOrder> findByUserIdAndStatus(Long userId, String status);

    List<UserOrder> findByStatus(String status);

}
