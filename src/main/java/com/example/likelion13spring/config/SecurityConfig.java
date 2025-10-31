package com.example.likelion13spring.config;

import com.example.likelion13spring.jwt.JwtAuthenticationFilter;
import com.example.likelion13spring.service.CustomOAuth2UserService;
import com.example.likelion13spring.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.Collections;

@Configuration //<- Spring 설정 파일로 간주, 내부의 메서드들은 자동으로 Bean으로 등록됨
@EnableWebSecurity //<- 해당 설정을 읽어 필터 체인 생성, 웹 보안 활성화
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService; // UserDetailsService DI. 의존성 주입
    private final CustomOAuth2UserService customOAuth2UserService; // week26 추가
    private final JwtAuthenticationFilter jwtFilter; //week27 추가

    private static void corsAllow(CorsConfigurer<HttpSecurity> corsConfigurer) {
        corsConfigurer.configurationSource(request -> {
            CorsConfiguration configuration = new CorsConfiguration();

            configuration.setAllowedMethods(Collections.singletonList("*")); // *로 모든 메서드 허용 (POST, GET 등으로 제한 가능)
            configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // 허용할 origin 설정. 여기선 프론트에서 오는 요청 허용
            configuration.setAllowedHeaders(Collections.singletonList("*")); // 허용할 HTTP request header 설정. 여기선 모든 헤더 허용
            configuration.setAllowCredentials(true); //자격 증명 허용 어부 (기본 값은 false)
            configuration.setMaxAge(3600L); // cors 응답 캐싱 시간 (동일한 origin에서 들어온 요청 처리 성능 향상). 여기선 1시간(3600초) 동안 오는 요청이 처리됨

            return configuration;
        });
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 설정 추가
        http
                .cors((SecurityConfig::corsAllow)) //CORS 설정
                .csrf(AbstractHttpConfigurer::disable) //csrf 공격 방지 비활성화 <- REST API는 HTTP 형식을 따라서 무상태(세션, 쿠키에 의존하지 않음)라서 ㄱㅊ
                .sessionManagement((manager) -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //세션 로그인 안함
                .httpBasic(AbstractHttpConfigurer::disable) // http basic auth 기반 로그인 인증창 뜨지 않게
                .formLogin(AbstractHttpConfigurer::disable) // 기본 로그인 페이지 없애기
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/join", "/login").permitAll() // 이 url에 대해선 모두 허용
                        .requestMatchers("/**").authenticated()) // 다른 url은 인증된 사용자만 허용
//                .oauth2Login(oauth -> oauth
//                        .userInfoEndpoint(userInfo -> userInfo
//                                .userService(customOAuth2UserService)
//                        )
//                )
//            .formLogin(Customizer.withDefaults()) // login 설정
//            .logout(Customizer.withDefaults()) // logout 설정
                .userDetailsService(customUserDetailsService);
        //UsernamePasswordAuthenticationFilter 전에 jwtFilter를 먼저 거치도록 추가함
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // 해당 필터 전에 jwtFilter가 걸리도록
        return http.build();

    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
