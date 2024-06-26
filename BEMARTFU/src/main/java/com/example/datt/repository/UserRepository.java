package com.example.datt.repository;

import com.example.datt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByStatusTrue();
    Optional<User> findByEmail(String username);

    Boolean existsByEmail(String email);
}
