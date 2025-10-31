package com.example.likelion13spring.jwt;

import com.example.likelion13spring.config.MemberAuthentication;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//요청 헤더에서 Authentication 토큰을 꺼냄 -> 유효하면 MemberAuthentication 생성하고 SecurityHolder에 저장함
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    //OncePerRequestFilter 상속함 <- HTTP request의 한 번의 요청에 대해, 한 번만 실행함

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            //토큰 받아오기
            final String token = getJwtFromRequest(request);
            //위에서 받아온 토큰이 유효한지 확인함. 유효성 검사
            JwtValidationType jwtValidationType = jwtTokenProvider.validateToken(token);
            if (jwtValidationType == JwtValidationType.VALID_JWT) {
                //jwtValidationType이 잘 가져와 졌다면 유저네임을 추출함(프로바이더에서 생성했던 access token으로 유저네임을 반환함)
                // 이 때 토큰을 넣어줌
                String username = jwtTokenProvider.getUsernameFromAccessToken(token);
                //유저네임을 통해 MemberAuthentication 객체를 만들어준다
                MemberAuthentication authentication = MemberAuthentication.createMemberAuthentication(username);
                //디테일 설정
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                //SecurityContextHolder에 context로 authentication을 설정함
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                log.error("현재 상태: " + jwtValidationType.toString());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        //다음 필터로 요청을 넘긴다
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization"); //헤더에서 받아옴
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            //Bearer로 시작하는 토큰이 유효하다면 앞에 붙어있는 말을 떼고 반환해줌
            return bearerToken.substring("Bearer ".length());
        }
        //아니라면 null 반환
        return null;
    }
}