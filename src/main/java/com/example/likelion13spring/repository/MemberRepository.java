package com.example.likelion13spring.repository;

import com.example.likelion13spring.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByName(String name); //이전 코드의 findByName
    //extend로 받아왔기 때문에 delete 등 다 만들어져 있음
    Optional<Member> findByEmail(String email);

    //18주차 과제 1 - 나이 20이상, 이름 오름차순, 페이징
    Page<Member> findByAgeGreaterThanEqualOrderByNameAsc(int age, Pageable pageable);
    //18주차 과제 2.1 - 이름이 특정 값으로 시작, 페이징, 이름 오름차순
    Page<Member> findByNameStartsWithOrderByNameAsc(String prefix, Pageable pageable);
    //18주차 과제 2.2 - 이름이 주어진 값으로 시작하는 경우만 필터링하려면? 리스트 사용하면 되나?
    List<Member> findByNameStartsWithOrderByNameAsc(String prefix);

} //Spring Data JPA가 자동으로 인식해서 내부적으로 JPQL 쿼리를 생성해준다!