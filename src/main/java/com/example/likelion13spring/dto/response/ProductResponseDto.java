package com.example.likelion13spring.dto.response;

import com.example.likelion13spring.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ProductResponseDto { //서버 -> 클라이언트 (반환)
    private Long id;
    private String name;
    private Integer price;
    private Integer stock;
    private String description;

    // 추가
    public static ProductResponseDto fromEntity(Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .stock(product.getStock())
                .description(product.getDescription())
                .build();
    }
}
//RequestDto에서는 `@Builder`를 붙여주지 않고 builder 패턴을 사용했는데,
// ResponseDto에서는 `@Builder`를 붙였음
//    -> RequestDto에서는 스프링이 HTTP 요청을 자동으로 자바 객체로 변환해주지만,
//    -> ResponseDto에서는 개발자가 엔티티에서 값을 꺼내 직접 dto를 생성하기 때문.
//근데 헷갈리면 그냥 둘 다 어노테이션 붙이기