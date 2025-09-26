package com.example.likelion13spring.repository;

import com.example.likelion13spring.domain.Member;
import com.example.likelion13spring.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Orders 엔티티에 대한 JPA Repository 인터페이스
public interface OrdersRepository extends JpaRepository<Orders, Long> {
    // 구매자(Member)별 주문 목록 조회용 메서드 정의
    List<Orders> findByBuyer(Member buyer);
}
