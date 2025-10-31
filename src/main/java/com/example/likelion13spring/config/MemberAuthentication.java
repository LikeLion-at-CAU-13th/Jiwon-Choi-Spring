package com.example.likelion13spring.config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

//jwt에서 파싱한 username으로 authentication을 만드는 과정
public class MemberAuthentication extends UsernamePasswordAuthenticationToken {
    public MemberAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public static MemberAuthentication createMemberAuthentication(String username) {
        return new MemberAuthentication(username, null, null);
    }
}