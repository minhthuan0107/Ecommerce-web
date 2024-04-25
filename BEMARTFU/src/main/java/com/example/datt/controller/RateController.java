package com.example.datt.controller;

import com.example.datt.entity.Rate;
import com.example.datt.repository.OrderDetailRepository;
import com.example.datt.repository.ProductRepository;
import com.example.datt.repository.RateRepository;
import com.example.datt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@CrossOrigin("*")
@RestController
@RequestMapping("api/rates")
public class RateController {
    @Autowired
    RateRepository rateRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Autowired
    ProductRepository productRepository;
    @GetMapping
    public ResponseEntity<List<Rate>> findAll() {
        return ResponseEntity.ok(rateRepository.findAllByOrderByIdDesc());
    }

    @GetMapping("{orderDetailId}")
    public ResponseEntity<Rate> findById(@PathVariable Long orderDetailId) {
        if (!orderDetailRepository.existsById(orderDetailId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(rateRepository.findByOrderDetail(orderDetailRepository.findById(orderDetailId).get()));
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<List<Rate>> findByProduct(@PathVariable("id") Long id) {
        if (!productRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(rateRepository.findByProductOrderByIdDesc(productRepository.findById(id).get()));
    }

    @PostMapping
    public ResponseEntity<Rate> post(@RequestBody Rate rate) {
        if (!userRepository.existsById(rate.getUser().getUserId())) {
            return ResponseEntity.notFound().build();
        }
        if (!productRepository.existsById(rate.getProduct().getProductId())) {
            return ResponseEntity.notFound().build();
        }
        if (!orderDetailRepository.existsById(rate.getOrderDetail().getOrderDetailId())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(rateRepository.save(rate));
    }

    @PutMapping
    public ResponseEntity<Rate> put(@RequestBody Rate rate) {
        if (!rateRepository.existsById(rate.getId())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(rateRepository.save(rate));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        if (!rateRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        rateRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }


}
