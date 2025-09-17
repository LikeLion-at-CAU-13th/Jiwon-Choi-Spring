package com.example.likelion13spring.dto.request;

import com.example.likelion13spring.domain.Member;
import com.example.likelion13spring.domain.Product;
import lombok.Getter;

@Getter //19주차 crud
public class ProductRequestDto { //클라이언트 -> 서버 (요청)
    private String name;
    private Integer price;
    private Integer stock;
    private String description;
    private Long memberId; // 판매자인지 확인하기 위함

    public Product toEntity(Member seller) { //DTO를 실제 엔티티 객체로 변환하는 역할
        return Product.builder() //builder패턴 사용중
                .name(this.name)
                .price(this.price)
                .stock(this.stock)
                .description(this.description)
                .seller(seller)
                .build();
    }
}