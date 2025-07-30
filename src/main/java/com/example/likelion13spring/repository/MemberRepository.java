package com.example.likelion13spring.repository;

import com.example.likelion13spring.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByName(String name); //이전 코드의 findByName
    //extend로 받아왔기 때문에 delete 등 다 만들어져 있음
    Optional<Member> findByEmail(String email);

} //Spring Data JPA가 자동으로 인식해서 내부적으로 JPQL 쿼리를 생성해준다!