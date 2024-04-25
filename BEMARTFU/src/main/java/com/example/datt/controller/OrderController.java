package com.example.datt.controller;

import com.example.datt.Service.implement.SendMail;
import com.example.datt.entity.*;
import com.example.datt.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("api/orders")
public class OrderController {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    CartDetailRepository cartDetailRepository;
    @Autowired
    OrderDetailRepository orderDetailRepository;
    @Autowired
    SendMail sendMail;
    @Autowired
    ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<List<Order>> findAll() {
        return ResponseEntity.ok(orderRepository.findAllByOrderByOrdersIdDesc());
    }
    @GetMapping("{id}")
    public ResponseEntity<Order> getById(@PathVariable("id") Long id) {
        if (!orderRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orderRepository.findById(id).get());
    }
    @GetMapping("/user/{email}")
    public ResponseEntity<List<Order>> getByUser(@PathVariable("email") String email) {
        if (!userRepository.existsByEmail(email)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity
                .ok(orderRepository.findByUserOrderByOrdersIdDesc(userRepository.findByEmail(email).get()));
    }
    @PostMapping("/{email}")
    public ResponseEntity<Order> checkout(@PathVariable("email") String email, @RequestBody Cart cart) {
        if (!userRepository.existsByEmail(email)) {
            return ResponseEntity.notFound().build();
        }
        if (!cartRepository.existsById(cart.getCartId())) {
            return ResponseEntity.notFound().build();
        }
        List<CartDetail> items = cartDetailRepository.findByCart(cart);
        Double amount = 0.0;
        for (CartDetail i : items) {
            amount += i.getPrice();
        }
        Order order = orderRepository.save(new Order(0L, new Date(), amount, cart.getAddress(), cart.getPhone(), 0 ,userRepository.findByEmail(email).get()));
        for (CartDetail i : items) {
            OrderDetail orderDetail = new OrderDetail(0L, i.getQuantity(), i.getPrice(), i.getProduct(), order);
            orderDetailRepository.save(orderDetail);
        }
//		cartDetailRepository.deleteByCart(cart);
        for (CartDetail i : items) {
            cartDetailRepository.delete(i);
        }
        sendMail.sendMailOrder(order);
        return ResponseEntity.ok(order);
    }
    @GetMapping("cancel/{orderId}")
    public ResponseEntity<Void> cancel(@PathVariable("orderId") Long id) {
        if (!orderRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Order order = orderRepository.findById(id).get();
        order.setStatus(3);
        orderRepository.save(order);
        sendMail.sendMailOrderCancel(order);
        return ResponseEntity.ok().build();
    }

    @GetMapping("deliver/{orderId}")
    public ResponseEntity<Void> deliver(@PathVariable("orderId") Long id) {
        if (!orderRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Order order = orderRepository.findById(id).get();
        order.setStatus(1);
        orderRepository.save(order);
        sendMail.sendMailOrderDeliver(order);
        return ResponseEntity.ok().build();
    }

    @GetMapping("success/{orderId}")
    public ResponseEntity<Void> success(@PathVariable("orderId") Long id) {
        if (!orderRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Order order = orderRepository.findById(id).get();
        order.setStatus(2);
        orderRepository.save(order);
        sendMail.sendMailOrderSuccess(order);
        updateProduct(order);
        return ResponseEntity.ok().build();
    }

    public void updateProduct(Order order) {
        List<OrderDetail> listOrderDetail = orderDetailRepository.findByOrder(order);
        for (OrderDetail orderDetail : listOrderDetail) {
            Product product = productRepository.findById(orderDetail.getProduct().getProductId()).get();
            if (product != null) {
                product.setQuantity(product.getQuantity() - orderDetail.getQuantity());
                product.setSold(product.getSold() + orderDetail.getQuantity());
                productRepository.save(product);
            }
        }
    }
}
