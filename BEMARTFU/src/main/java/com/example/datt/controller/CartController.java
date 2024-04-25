package com.example.datt.controller;

import com.example.datt.entity.Cart;
import com.example.datt.repository.CartDetailRepository;
import com.example.datt.repository.CartRepository;
import com.example.datt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
    @RestController
    @RequestMapping("api/cart")
    public class CartController {
        @Autowired
        CartRepository cartRepository;

        @Autowired
        CartDetailRepository cartDetailRepository;

        @Autowired
        UserRepository userRepository;

        @GetMapping("/user/{email}")
        public ResponseEntity<Cart> getCartUser(@PathVariable("email") String email) {
            if (!userRepository.existsByEmail(email)) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(cartRepository.findByUser(userRepository.findByEmail(email).get()));
        }
        @GetMapping()
        public ResponseEntity<List<Cart>> getCartAll() {
            return ResponseEntity.ok(cartRepository.findAll());
        }

        @PutMapping("/user/{email}")
        public ResponseEntity<Cart> putCartUser(@PathVariable("email") String email, @RequestBody Cart cart) {
            if (!userRepository.existsByEmail(email)) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(cartRepository.save(cart));
        }

    }

