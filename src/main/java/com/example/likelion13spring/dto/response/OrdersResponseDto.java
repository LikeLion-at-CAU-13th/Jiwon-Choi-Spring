package com.example.likelion13spring.dto.response;

import com.example.likelion13spring.enums.DeliverStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class OrdersResponseDto {

    private Long id; // 주문 ID
    private DeliverStatus deliverStatus; // 배송 상태

    private ShippingAddressDto shippingAddress; // 배송 정보

    private List<ProductInfo> products; // 주문 상품 정보 목록

    // 배송 정보 DTO (내부 클래스)
    @Getter
    @Builder
    @AllArgsConstructor
    public static class ShippingAddressDto {
        private String recipient;
        private String phoneNumber;
        private String roadAddress;
        private String detailAddress;
        private String postCode;
    }

    // 주문 상품 정보 DTO (내부 클래스)
    @Getter
    @Builder
    @AllArgsConstructor
    public static class ProductInfo {
        private Long productId; // 상품 ID
        private String name; // 상품명
        private Integer quantity; // 주문 수량
    }
}
