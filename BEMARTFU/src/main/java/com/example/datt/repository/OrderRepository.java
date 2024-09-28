package com.example.datt.repository;

import com.example.datt.entity.Order;
import com.example.datt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByOrdersIdDesc(User user);

    List<Order> findAllByOrderByOrdersIdDesc();

    List<Order> findByStatus(int status);
    @Modifying
    @Transactional
    @Query("DELETE FROM Order o WHERE o.user.userId = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}
