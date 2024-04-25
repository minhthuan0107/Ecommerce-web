package com.example.datt.controller;
import com.example.datt.entity.Category;
import com.example.datt.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("api/categories")
@CrossOrigin("*")
public class CategoryController {
    @Autowired
    CategoryRepository categoryRepository;
    @GetMapping
    public ResponseEntity<List<Category>> getAll() {
        return ResponseEntity.ok(categoryRepository.findAll());
    }
    @GetMapping("{id}")
    public ResponseEntity<Category> getById(@PathVariable("id") Long id) {
        if (!categoryRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(categoryRepository.findById(id).get());
    }

    @PostMapping
    public ResponseEntity<Category> post(@RequestBody Category category) {
        if (categoryRepository.existsById(category.getCategoryId())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(categoryRepository.save(category));
    }

    @PutMapping("{id}")
    public ResponseEntity<Category> put(@RequestBody Category category, @PathVariable("id") Long id) {
        if (!id.equals(category.getCategoryId())) {
            return ResponseEntity.badRequest().build();
        }
        if (!categoryRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(categoryRepository.save(category));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        if (!categoryRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        categoryRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }


}
