package com.example.datt.controller;

import com.example.datt.entity.Category;
import com.example.datt.entity.Product;
import com.example.datt.repository.CategoryRepository;
import com.example.datt.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/products")
@CrossOrigin("*")
public class ProductController {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok(productRepository.findByStatusTrue());
    }
    @GetMapping("{id}")
    public ResponseEntity<Product> getById(@PathVariable("id") Long id){
        if(!productRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
      return ResponseEntity.ok(productRepository.findById(id).get());
    }
    @GetMapping("bestseller")
    public ResponseEntity<List<Product>> getBestSeller() {
        return ResponseEntity.ok(productRepository.findByStatusTrueOrderBySoldDesc());
    }
    @GetMapping("bestseller-admin")
    public ResponseEntity<List<Product>> getBestSellerAdmin() {
        return ResponseEntity.ok(productRepository.findTop10ByOrderBySoldDesc());
    }

    @GetMapping("category/{id}")
    public ResponseEntity<List<Product>> getByCategory(@PathVariable("id") Long id) {
        if (!categoryRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Category category = categoryRepository.findById(id).get();
        return ResponseEntity.ok(productRepository.findByCategory(category));
    }
    @GetMapping("latest")
    public ResponseEntity<List<Product>> getLasted() {
        return ResponseEntity.ok(productRepository.findByStatusTrueOrderByEnteredDateDesc());
    }

    @GetMapping("rated")
    public ResponseEntity<List<Product>> getRated() {
        return ResponseEntity.ok(productRepository.findProductRated());
    }
    @PostMapping
    public  ResponseEntity<Product> addProduct(@RequestBody Product product){
     if(productRepository.existsById(product.getProductId())){
         return  ResponseEntity.badRequest().build();
     }
     return ResponseEntity.ok(productRepository.save(product));
    }
    @PutMapping("{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") Long id, @RequestBody Product product) {
        if (!id.equals(product.getProductId())) {
            return ResponseEntity.badRequest().build();
        }
        if (!productRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Product updatedProduct = productRepository.save(product);
        return ResponseEntity.ok(updatedProduct);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable("id") Long id){
        if(!productRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        productRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping("suggest/{categoryId}/{productId}")
    public ResponseEntity<List<Product>> suggest(@PathVariable("categoryId") Long categoryId,
                                                 @PathVariable("productId") Long productId) {
        return ResponseEntity.ok(productRepository.findProductSuggest(categoryId, productId, categoryId, categoryId));
    }

}
