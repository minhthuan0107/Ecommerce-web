package com.example.datt.repository;
import com.example.datt.entity.Cart;
import com.example.datt.entity.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, Long> {

	List<CartDetail> findByCart(Cart cart);

	void deleteByCart(Cart cart);

}
