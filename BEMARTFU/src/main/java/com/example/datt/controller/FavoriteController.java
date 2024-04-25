package com.example.datt.controller;
import com.example.datt.entity.Favorite;
import com.example.datt.entity.Product;
import com.example.datt.entity.User;
import com.example.datt.repository.FavoriteRepository;
import com.example.datt.repository.ProductRepository;
import com.example.datt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
    @CrossOrigin("*")
    @RestController
    @RequestMapping("api/favorites")
    public class FavoriteController {
        @Autowired
        FavoriteRepository favoriteRepository;

        @Autowired
        UserRepository userRepository;

        @Autowired
        ProductRepository productRepository;

        @GetMapping("email/{email}")
        public ResponseEntity<List<Favorite>> findByEmail(@PathVariable("email") String email) {
            if (userRepository.existsByEmail(email)) {
                return ResponseEntity.ok(favoriteRepository.findByUser(userRepository.findByEmail(email).get()));
            }
            return ResponseEntity.notFound().build();
        }

        @GetMapping("product/{id}")
        public ResponseEntity<Integer> findByProduct(@PathVariable("id") Long id) {
            if (productRepository.existsById(id)) {
                return ResponseEntity.ok(favoriteRepository.countByProduct(productRepository.getById(id)));
            }
            return ResponseEntity.notFound().build();
        }

        @GetMapping("{productId}/{email}")
        public ResponseEntity<Favorite> findByProductAndUser(@PathVariable("productId") Long productId,
                                                             @PathVariable("email") String email) {
            if (userRepository.existsByEmail(email)) {
                if (productRepository.existsById(productId)) {
                    Product product = productRepository.findById(productId).get();
                    User user = userRepository.findByEmail(email).get();
                    return ResponseEntity.ok(favoriteRepository.findByProductAndUser(product, user));
                }
            }
            return ResponseEntity.notFound().build();
        }

        @PostMapping("email")
        public ResponseEntity<Favorite> post(@RequestBody Favorite favorite) {
            return ResponseEntity.ok(favoriteRepository.save(favorite));
        }

        @DeleteMapping("{id}")
        public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
            if (favoriteRepository.existsById(id)) {
                favoriteRepository.deleteById(id);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.notFound().build();
        }

    }


