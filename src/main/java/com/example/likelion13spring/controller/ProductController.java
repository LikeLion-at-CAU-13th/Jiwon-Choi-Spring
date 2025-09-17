package com.example.likelion13spring.controller;

import com.example.likelion13spring.dto.request.ProductDeleteRequestDto;
import com.example.likelion13spring.dto.request.ProductRequestDto;
import com.example.likelion13spring.dto.request.ProductUpdateRequestDto;
import com.example.likelion13spring.dto.response.ProductResponseDto;
import com.example.likelion13spring.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products") // 공통 경로 지정
public class ProductController {

    private final ProductService productService;

    @PostMapping //<- post를 쓸 것임 & 각각의 crud에 대한 매핑 후 추가 경로 지정...?@@설명 추가하기
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductRequestDto dto) {
        //@RequestBody <- ProductRequestDto의 구조대로 넣어야 함
        return ResponseEntity.ok(productService.createProduct(dto));
    }

    // 전체 상품 조회
    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    // 특정 상품 조회
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        //Long id를 받아옴... 위의 {id}로 받아옴
        return ResponseEntity.ok(productService.getProductById(id));
    }

    // 특정 상품 업데이트 - 여기는 body가 필요해서 @RequestBody까지 작성함!
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable Long id,
                                                            @RequestBody ProductUpdateRequestDto dto) {
        return ResponseEntity.ok(productService.updateProduct(id, dto));
    }

    // 특정 상품 삭제 - 이것도 body가 필요함 (아직 로그인 전이니까 임시로! 토큰을 쓸 수 없으니까)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id,
                                                @RequestBody ProductDeleteRequestDto dto) {
        productService.deleteProduct(id, dto);
        return ResponseEntity.ok("상품이 성공적으로 삭제되었습니다.");
    }
}
