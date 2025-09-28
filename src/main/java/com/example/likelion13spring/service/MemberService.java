package com.example.likelion13spring.service;

import com.example.likelion13spring.domain.Member;
import com.example.likelion13spring.dto.request.JoinRequestDto;
import com.example.likelion13spring.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Page<Member> getMembersByPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        return memberRepository.findAll(pageable);
    }

    //18주차 과제 1 - 나이 20이상, 이름 오름차순, 페이징
    public Page<Member> getAdultMembersSortedByName(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        return memberRepository.findByAgeGreaterThanEqualOrderByNameAsc(20, pageable);
    }
    //18주차 과제 2.1 - 이름이 특정 값으로 시작, 페이징, 이름 오름차순
    public Page<Member> getMembersByNamePrefix(String prefix, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        return memberRepository.findByNameStartsWithOrderByNameAsc(prefix, pageable);
    }
    //18주차 과제 2.2 - 이름이 특정 값으로 시작 - 리스트 사용한 경우
    public List<Member> getMembersByNamePrefix(String prefix) {
        return memberRepository.findByNameStartsWithOrderByNameAsc(prefix);
    }

    // 25주차 - 비밀번호 인코더 DI(생성자 주입)
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void join(JoinRequestDto joinRequestDto) {
        // 해당 name이 이미 존재하는 경우
        if (memberRepository.existsByName(joinRequestDto.getName())) {
            // 25주차 과제 - 이름 중복 예외처리
            throw new IllegalStateException("이미 존재하는 이름입니다.");
        }

        // 유저 객체 생성
        Member member = joinRequestDto.toEntity(bCryptPasswordEncoder);

        // 유저 정보 저장
        memberRepository.save(member);
    }

}