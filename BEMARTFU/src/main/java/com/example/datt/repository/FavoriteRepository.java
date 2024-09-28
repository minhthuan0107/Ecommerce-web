package com.example.datt.repository;

import com.example.datt.entity.Favorite;
import com.example.datt.entity.Product;
import com.example.datt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUser(User user);

    Integer countByProduct(Product product);

    Favorite findByProductAndUser(Product product, User user);

    @Modifying
    @Transactional
    @Query("DELETE FROM Favorite f WHERE f.user.userId = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}
