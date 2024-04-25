package com.example.datt.controller;

import com.example.datt.entity.OrderDetail;
import com.example.datt.repository.OrderDetailRepository;
import com.example.datt.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("api/orderDetail")
public class OrderDetailsController {
    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Autowired
    OrderRepository orderRepository;

    @GetMapping("/order/{id}")
    public ResponseEntity<List<OrderDetail>> getByOrder(@PathVariable("id") Long id) {
        if (!orderRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orderDetailRepository.findByOrder(orderRepository.findById(id).get()));
    }

}
