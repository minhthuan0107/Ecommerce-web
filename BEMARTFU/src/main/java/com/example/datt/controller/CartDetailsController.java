package com.example.datt.controller;
import com.example.datt.dto.CartDetailDto;
import com.example.datt.entity.CartDetail;
import com.example.datt.entity.Product;
import com.example.datt.repository.CartDetailRepository;
import com.example.datt.repository.CartRepository;
import com.example.datt.repository.ProductRepository;
import com.example.datt.services.CartDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
    @RestController
    @RequestMapping("api/cartDetail")
    public class CartDetailsController {

        @Autowired
        CartDetailRepository cartDetailRepository;

        @Autowired
        CartRepository cartRepository;

        @Autowired
        ProductRepository productRepository;
        @Autowired
        CartDetailService cartDetailService;


        @GetMapping("cart/{id}")
        public ResponseEntity<ApiResponse> getAllCartDetailsByCartId(@PathVariable("id") Long id) {
            if (id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse("Invalid cart ID: " + id, null));
            }
            if(!cartRepository.existsById(id)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).
                        body(new ApiResponse("Cart not found with id:" +id ,null));
            }
         List<CartDetailDto> cartDetailDtos = cartDetailService.getAllCartDetailsByCartId(id);
         return ResponseEntity.ok(new ApiResponse("Cart details retrieved successfully",cartDetailDtos));
        }



        @GetMapping("{id}")
        public ResponseEntity<ApiResponse> getCartDetailById(@PathVariable("id") Long id) {
            if (!cartDetailRepository.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).
                        body(new ApiResponse("CartDetail not found with id:" +id ,null));
            }
            return cartDetailService.getCartDetailById(id)
                    .map(cartDetail -> ResponseEntity.ok(new ApiResponse("Cart detail retrieved successfully",cartDetail)))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).
                            body(new ApiResponse("Cart detail not found", null)));

        }
        @PostMapping()
        public ResponseEntity<ApiResponse> postCartDetail(@RequestBody CartDetailDto detailDto) {
            //Kiểm tra Cart có tồn tại trong CSDL hay không
            if (!cartRepository.existsById(detailDto.getCartDto().getCartId())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("Cart not found with id:" ,null));
            }
            //Kiểm tra Product có tồn tại trong sản phẩm hay không
            Long productId = detailDto.getProductDto().getProductId();
            if(!cartDetailService.productIsValid(productId)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("Product not found or inactive with id:",null));
            }

            //Tìm kiếm detailId đã tồn tại hay chưa, nếu tồn tại thì update còn không thì thì add
            Optional<CartDetailDto> existingCartDetail = cartDetailService.getCartDetailById(detailDto.getCartDetailId());
            String message;
            CartDetailDto saveDetailDto ;
            if (existingCartDetail.isPresent()) {
                // Cập nhật bản ghi nếu đã tồn tại
                saveDetailDto = cartDetailService.saveOrUpdateCartDetail(detailDto);
                message = "Updated CartDetail";
            } else {
                // Thêm mới bản ghi nếu không tìm thấy
                saveDetailDto = cartDetailService.saveOrUpdateCartDetail(detailDto);
                message = "Added new CartDetail";
            }
            return ResponseEntity.ok(new ApiResponse(message,saveDetailDto));
        }

        @PutMapping()
        public ResponseEntity<CartDetail> put(@RequestBody CartDetail detail) {
            if (!cartRepository.existsById(detail.getCart().getCartId())) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(cartDetailRepository.save(detail));
        }

        @DeleteMapping("{id}")
        public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
            if (!cartDetailRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            cartDetailRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }

    }
