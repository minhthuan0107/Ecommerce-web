package com.example.datt.controller;
import com.example.datt.dto.CartDto;
import com.example.datt.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin("*")
    @RestController
    @RequestMapping("api/cart")
    public class CartController {
    @Autowired
    CartService cartService;
    @GetMapping("/user/{email}")
    public ResponseEntity<ApiResponse> getCartUserEmail(@PathVariable("email") String email) {
        if (!cartService.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body(new ApiResponse("Cart not found for the provided email", null));
        }
        return cartService.getCartByEmail(email)
                .map(cart -> ResponseEntity.ok(new ApiResponse("Cart found",cart))).
                orElseGet(()-> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Cart not found", null)));
    }
        @GetMapping()
        public ResponseEntity<ApiResponse> getCartAll() {
        List<CartDto> cartDtos = cartService.getAllCart();
            return ResponseEntity.ok(new ApiResponse("Cart list has been found",cartDtos));
        }

        @PutMapping("/user/{email}")
        public ResponseEntity<ApiResponse> putCartUser(@PathVariable("email") String email, @RequestBody CartDto cartDto) {
            if (!cartService.existsByEmail(email)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).
                        body(new ApiResponse("Cart not found for the provided email",null));
            }
            if (!email.equals(cartDto.getUserDto().getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Email in URL does not match email in request body",null));
            }
            return cartService.updateCartByEmail(email, cartDto)
                    .map(updateCart -> ResponseEntity.ok(new ApiResponse("Cart updated successfully", updateCart)))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Cart not found", null)));
            }
    }

