package com.example.datt.repository;

import com.example.datt.entity.Favorite;
import com.example.datt.entity.Product;
import com.example.datt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUser(User user);

    Integer countByProduct(Product product);

    Favorite findByProductAndUser(Product product, User user);
}
