package com.example.likelion13spring.service;

import com.example.likelion13spring.domain.Member;
import com.example.likelion13spring.enums.Role;
import com.example.likelion13spring.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class MemberServiceTest {
    @Autowired //?
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll(); //기존의 것들을 모두 지우고 시작

        IntStream.rangeClosed(1, 30).forEach(i -> { //30개
            Member member = Member.builder()
                    .name("user" + i)
                    .email("user" + i + "@test.com")
                    .address("서울시 테스트동 " + i + "번지")
                    .phoneNumber("010-1234-56" + String.format("%02d", i))
                    .deposit(1000 * i)
                    .isAdmin(false)
                    .role(Role.BUYER)
                    .build(); //객체 생성 완료

            memberRepository.save(member); //저장
        });
    }

    @Test
    void testGetMembersByPage() {
        Page<Member> page = memberService.getMembersByPage(0, 10); //10개씩 가져옴

        assertThat(page.getContent()).hasSize(10); //10개가 맞는지
        assertThat(page.getTotalElements()).isEqualTo(30); //총 개수 30개 맞는지
        assertThat(page.getTotalPages()).isEqualTo(3); //3페이지가 맞는지
        assertThat(page.getContent().get(0).getName()).isEqualTo("user1"); //첫 번째로 가져오는 user의 이름이 user1 맞는지
    }
}
