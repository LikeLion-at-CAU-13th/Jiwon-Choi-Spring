package com.example.likelion13spring.service;

import com.example.likelion13spring.domain.Member;
import com.example.likelion13spring.enums.Role;
import com.example.likelion13spring.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.util.List;
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
                    .age(i) //18주차 과제 - 나이 부분 추가! 1세부터 30세까지 순차로 생성됨
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

    //18주차 과제 1 - Member의 나이가 20세 이상인 경우만 조회, 이름기준 오름차순 정렬 페이징
    @Test
    void testGetAdultMembersSortedByName() {
        Page<Member> page = memberService.getAdultMembersSortedByName(0, 10);

        //여기에선 샘플 데이터 중 20~30살까지에 해당하니까... 11명임을 보장하는지 (생략해도 돌아가나@?)
        assertThat(page.getTotalElements()).isEqualTo(11);
        assertThat(page.getTotalPages()).isEqualTo(2); //2페이지가 맞는지
        assertThat(page.getContent().get(0).getName()).isEqualTo("user20"); //현재 페이지에 담긴 첫 번째의 이름이 user20인지 검증
        assertThat(page.getContent().get(9).getName()).isEqualTo("user29"); //현재 페이지 중 마지막 객체의 이름이 user29인지 검증

        //두 번째 페이지의 첫 멤버가 user30인지 확인하려면?
        Page<Member> page2 = memberService.getAdultMembersSortedByName(1, 10);
        assertThat(page2.getContent().get(0).getName()).isEqualTo("user30");

    }

    //18주차 과제 2.1 - Member의 이름이 주어진 값으로 시작하는 경우만 필터링하는 로직
    @Test
    void testGetMembersByNamePrefix() {
        Page<Member> page = memberService.getMembersByNamePrefix("user1", 0, 20);
        assertThat(page.getTotalElements()).isEqualTo(11); // user1이랑 user10~user19까지니까
        assertThat(page.getContent().get(0).getName()).isEqualTo("user1");
        assertThat(page.getContent().get(10).getName()).isEqualTo("user19");
    }

    //18주차 과제 2.2 - 이름이 주어진 값으로 시작하는 경우 필터링 - 리스트 사용한 경우
    @Test
    void testGetMembersByNamePrefixList() {
        List<Member> members = memberService.getMembersByNamePrefix("user1");
        assertThat(members.get(0).getName()).isEqualTo("user1");
        assertThat(members.size()).isEqualTo(11); // user1랑 user10~user19까지
        assertThat(members.get(0).getName()).isEqualTo("user1");
        assertThat(members.get(1).getName()).isEqualTo("user10");
    }
}
