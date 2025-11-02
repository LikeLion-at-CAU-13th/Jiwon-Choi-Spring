package com.example.likelion13spring.repository;

import com.example.likelion13spring.domain.Member;
import com.example.likelion13spring.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    //27주차 과제 - 판매자 기준 상품찾기
    List<Product> findBySeller(Member seller);
}