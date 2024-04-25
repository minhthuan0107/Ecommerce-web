package com.example.datt.repository;

import com.example.datt.entity.Order;
import com.example.datt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByOrdersIdDesc(User user);

    List<Order> findAllByOrderByOrdersIdDesc();

    List<Order> findByStatus(int status);
}
