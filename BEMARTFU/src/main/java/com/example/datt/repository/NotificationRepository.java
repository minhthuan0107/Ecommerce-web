package com.example.datt.repository;

import com.example.datt.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByOrderByIdDesc();

    @Modifying
    @Query(value = "update notification set status = true", nativeQuery = true)
    void readAll();
}
