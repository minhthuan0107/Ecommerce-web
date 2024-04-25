package com.example.datt.repository;

import com.example.datt.entity.OrderDetail;
import com.example.datt.entity.Product;
import com.example.datt.entity.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface RateRepository extends JpaRepository<Rate, Long> {
    List<Rate> findAllByOrderByIdDesc();

    Rate findByOrderDetail(OrderDetail orderDetail);

    List<Rate> findByProductOrderByIdDesc(Product product);
}
