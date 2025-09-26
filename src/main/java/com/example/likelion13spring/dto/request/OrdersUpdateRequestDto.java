package com.example.likelion13spring.dto.request;

import lombok.Getter;

@Getter
public class OrdersUpdateRequestDto {

    // 배송정보 수정용 필드들
    private String recipient; // 수령인 이름
    private String phoneNumber; // 전화번호
    private String roadAddress; // 도로명 주소
    private String detailAddress; // 상세 주소
    private String postCode; // 우편번호
}
