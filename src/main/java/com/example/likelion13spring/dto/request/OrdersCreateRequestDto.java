package com.example.likelion13spring.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class OrdersCreateRequestDto {

    private Long buyerId; // 주문자 ID (구매자)

    private String recipient; // 수령인 이름
    private String phoneNumber; // 수령인 전화번호
    private String roadAddress; // 도로명 주소
    private String detailAddress; // 상세 주소
    private String postCode; // 우편번호

    // 주문할 상품 목록 (상품ID와 수량 포함)
    private List<OrderProductDto> products;

    @Getter
    public static class OrderProductDto {
        private Long productId; // 상품 ID
        private Integer quantity; // 주문 수량
    }
}

