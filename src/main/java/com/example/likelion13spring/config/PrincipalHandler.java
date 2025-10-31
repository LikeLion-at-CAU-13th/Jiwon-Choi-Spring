package com.example.likelion13spring.config;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

//Principal로 username을 받아옴
//principal : 사용자의 고유한 정보를 받아올 수 있도록 인증하는 정보들
// jwt 사용 시 내 정보를 찾을 때 memberid 말고 토큰을 통해 내 정보 조회 가능함
@Component
public class PrincipalHandler {
    public static String getUsernameFromPrincipal() {
        //securityContextHolder의 Context의 Authentication에 접근해 가져옴
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.toString(); //그걸 string으로 반환함
    }
}