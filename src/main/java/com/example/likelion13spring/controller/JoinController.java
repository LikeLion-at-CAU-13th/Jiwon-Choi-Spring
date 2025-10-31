package com.example.likelion13spring.controller;

import com.example.likelion13spring.config.PrincipalHandler;
import com.example.likelion13spring.domain.Member;
import com.example.likelion13spring.dto.request.JoinRequestDto;
import com.example.likelion13spring.dto.response.TokenResponseDto;
import com.example.likelion13spring.jwt.JwtTokenProvider;
import com.example.likelion13spring.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

//25주차
@RestController
@RequiredArgsConstructor
public class JoinController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider; //27주차

    @PostMapping("/join")
    public void join(@RequestBody JoinRequestDto joinRequestDto) {
        memberService.join(joinRequestDto);
    }

    //27주차
    @PostMapping("/login")
    public TokenResponseDto login(@RequestBody JoinRequestDto joinRequestDto) {
        Member member = memberService.login(joinRequestDto); //로그인해서 멤버 객체를 받아옴
        //멤버네임으로 accesstoken을 받아오고, refreshtoken도 받아옴
        return TokenResponseDto.of(jwtTokenProvider.generateAccessToken(member.getName()), jwtTokenProvider.generateRefreshToken(member.getName()));
    }

    //내 이름을 출력하는 기능
    @GetMapping("/my")
    public ResponseEntity<String> getMyName() {
        String name = PrincipalHandler.getUsernameFromPrincipal();
        return ResponseEntity.ok(name);
    }

}