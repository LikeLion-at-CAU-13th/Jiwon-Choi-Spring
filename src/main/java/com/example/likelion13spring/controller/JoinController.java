package com.example.likelion13spring.controller;

import com.example.likelion13spring.dto.request.JoinRequestDto;
import com.example.likelion13spring.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

//25주차
@RestController
@RequiredArgsConstructor
public class JoinController {

    private final MemberService memberService;

    @PostMapping("/join")
    public void join(@RequestBody JoinRequestDto joinRequestDto) {
        memberService.join(joinRequestDto);
    }

}