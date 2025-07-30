package com.example.likelion13spring.domain;

//
import com.example.likelion13spring.enums.Role;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String email;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role; // 판매자면 SELLER, 구매자면 BUYER

    private Boolean isAdmin; // 관리자 계정 여부

    private Integer deposit; // 현재 계좌 잔액

    //연관관계의 주인은 Product라는 클래스
    //영속관계 설정
    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private Set<Product> products = new HashSet<>();

    public void chargeDeposit(int money){
        this.deposit += money;
    }
    public void useDeposit(int money) {
        this.deposit -= money;
    }
    //18주차 추가 - 생성자에만 빌더를 붙이는 경우
    @Builder
    public Member(String name, String address, String email, String phoneNumber,
                  Role role, Boolean isAdmin, Integer deposit) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.isAdmin = isAdmin;
        this.deposit = deposit;
    }
}
