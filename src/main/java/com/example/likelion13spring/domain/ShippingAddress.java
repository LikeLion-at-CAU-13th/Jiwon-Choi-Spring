package com.example.likelion13spring.domain;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 배송정보를 별도의 Embeddable 타입으로 분리하여 Orders에 포함하기 위함
@Embeddable
@Getter
@NoArgsConstructor
public class ShippingAddress {

    private String recipient;     // 수령인 이름
    private String phoneNumber;   // 전화번호
    private String roadAddress;   // 도로명 주소
    private String detailAddress; // 상세 주소
    private String postCode;      // 우편번호

    // 빌더 패턴 생성자
    @Builder
    public ShippingAddress(String recipient, String phoneNumber, String roadAddress, String detailAddress, String postCode) {
        this.recipient = recipient;
        this.phoneNumber = phoneNumber;
        this.roadAddress = roadAddress;
        this.detailAddress = detailAddress;
        this.postCode = postCode;
    }
}